package io.eschmann.zmittag.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import io.eschmann.zmittag.entities.Group;
import io.eschmann.zmittag.entities.Member;
import io.eschmann.zmittag.entities.Restaurant;
import io.eschmann.zmittag.entities.Tag;
import io.eschmann.zmittag.persistence.ConnectionManager;
import io.eschmann.zmittag.persistence.GroupDao;
import io.eschmann.zmittag.persistence.MemberDao;
import io.eschmann.zmittag.persistence.RestaurantDao;
import io.eschmann.zmittag.persistence.TagDao;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mongodb.morphia.query.UpdateResults;

public class DbTests {

	private static ConnectionManager connectionManager;
	private static GroupDao groupDao;
	private static MemberDao memberDao;
	private static RestaurantDao restaurantDao;
	private static TagDao tagDao;

	@BeforeClass
	public static void beforeClass() throws UnknownHostException {
		connectionManager = new ConnectionManager();
		groupDao = new GroupDao(connectionManager);
		// groupDao.getCollection().drop();
		memberDao = new MemberDao(connectionManager);
		// memberDao.getCollection().drop();
		restaurantDao = new RestaurantDao(connectionManager);
		tagDao = new TagDao(connectionManager);
	}

	@Before
	public void before() {

	}

	@Test
	public void testShouldSaveAndFindSimpleGroup() throws UnknownHostException {

		Group testGroup = createTestGroup();
		Set<String> members = new HashSet<String>(Arrays.asList(new String[] {
				"one", "two", "three" }));
		testGroup.setMembers(members);

		groupDao.save(testGroup);

		Group foundGroup = groupDao.findOneGroup(testGroup.getId());
		assertEquals(testGroup.getId(), foundGroup.getId());

	}

	@Test
	public void testShouldSaveAndFindSimpleMember() throws UnknownHostException {

		Member testMember = createTestMember();

		memberDao.save(testMember);

		Member foundMember = memberDao.findOneMember(testMember.getId());
		assertEquals(testMember.getId(), foundMember.getId());

	}

	@Test
	public void testShouldSaveAndFindSimpleRestaurant() {
		Restaurant testRestaurant = createTestRestaurant();
		restaurantDao.save(testRestaurant);

		Restaurant foundRestaurant = restaurantDao
				.findOneRestaurant(testRestaurant.getId());

		assertEquals(testRestaurant.getId(), foundRestaurant.getId());
	}

	@Test
	public void tetsShouldAddMemberToGroup() {

		Group testGroup = createTestGroup();
		groupDao.save(testGroup);

		String newMemberName = createTestName();

		final UpdateResults result = groupDao.addMemberToGroup(
				testGroup.getId(), newMemberName);

		assertEquals(1, result.getUpdatedCount());

		Group foundGroup = groupDao.findOneGroup(testGroup.getId());

		assertTrue(foundGroup.getMembers().contains(newMemberName));
	}

	@Test
	public void testShouldUpdateRestaurantRating() {
		Restaurant testRestaurant = new Restaurant();
		testRestaurant.setName("Restaurant " + createTestName());
		testRestaurant.setLocation(createTestDouble(), createTestDouble());
		testRestaurant.addTag(createTestName());
		testRestaurant.setAverageRating(4.0d);
		testRestaurant.setRatingCount(1);

		restaurantDao.save(testRestaurant);

		restaurantDao.addRatingToRestaurant(testRestaurant.getId(), 2.0d);

		Restaurant foundRestaurant = restaurantDao
				.findOneRestaurant(testRestaurant.getId());
		assertEquals(0,
				Double.compare(3.0d, foundRestaurant.getAverageRating()));
	}

	@Test
	public void testShouldFindNearestRestaurants() {
		Restaurant testRestaurant = createTestRestaurant();
		double latitude = 25.0d;
		double longitude = 15.0d;
		testRestaurant.setLocation(latitude, longitude);
		String testTag = createTestName();
		testRestaurant.addTag(testTag);

		restaurantDao.save(testRestaurant);

		List<Restaurant> foundRestaurants = restaurantDao
				.findNearestRestaurants(latitude, longitude);
		assertTrue(foundRestaurants.size() > 0);
	}

	@Test
	public void testShouldSaveAndFindMember() {
		Member member = new Member();
		member.setName("Fabian");
		member.setEmail("fabian.eschmann@zmittag.io");
		memberDao.save(member);

		Member foundMember = memberDao.findOneMember(member.getId());
		assertEquals(member.getId(), foundMember.getId());
	}

	@Test
	public void testShouldInsertRestaurantsFromFile() throws IOException, URISyntaxException {
	
		URL resource = Restaurant.class.getResource("/resources/restaurants.csv");
		File restaurantFile = Paths.get(resource.toURI()).toFile();
		
		
		List<String> restaurantLines = Files.readAllLines(
				restaurantFile.toPath(), Charset.defaultCharset());

		for (String line : restaurantLines) {
			String[] restaurantData = line.split(";");
			if(restaurantData.length != 4) {
				continue;
			}
			
			Restaurant restaurant = new Restaurant();
			restaurant.setName(restaurantData[0]);
			restaurant.setLocation(Double.valueOf(restaurantData[1]), Double.valueOf(restaurantData[2]));
			restaurant.addTag(restaurantData[3]);
			restaurant.setRatingCount(1);
			restaurant.setAverageRating(4.0d);
			restaurantDao.save(restaurant);

			Restaurant foundRestaurant = restaurantDao
					.findOneRestaurant(restaurant.getId());
			assertEquals(restaurant.getId(), foundRestaurant.getId());
		}
	}

	@Test
	public void testShouldSaveAndFindGroup() {
		Group group = new Group();
		group.setName("Greens");
		group.setDate(new Date().getTime());
		group.addMember("Fabian");

		groupDao.save(group);

		Group foundGroup = groupDao.findOneGroup(group.getId());
		assertEquals(group.getId(), foundGroup.getId());
	}

	@Test
	public void testShouldSaveAndFindTags() {
		
		List<String> tags = Arrays.asList(new String[]{"Swiss","International","Creative","Extravagance","Greek","Simple"});
		
		for(String tagName : tags) {
			
			Tag tag = new Tag();
			tag.setName(tagName);		

			tagDao.addTagIfNotExist(tagName);
			
		}
	}
	
	@Test
	public void testShouldFindRestaurantsByTag() {
		String searchTag = "International";
		List<Restaurant> foundRestaurants = restaurantDao.findByTag(searchTag);
		assertFalse(foundRestaurants.isEmpty());
	}
	
	@Test
	public void testShouldSearchRestaurantNames() {
		final String pattern = "Moritz";
		List<Restaurant> foundRestaurants = restaurantDao.searchByName(pattern);
		assertFalse(foundRestaurants.isEmpty());
	}

	private Group createTestGroup() {
		Group group = new Group();
		group.setName(createTestName());
		return group;
	}

	private String createTestName() {
		return String.valueOf(new Random().nextInt(100000));
	}

	private Member createTestMember() {
		Member member = new Member();
		String name = createTestName();
		member.setName(name);
		member.setEmail(name + "@zmittag.io");
		return member;
	}

	private Restaurant createTestRestaurant() {
		Restaurant restaurant = new Restaurant();
		restaurant.setName("Restaurant " + createTestName());
		return restaurant;
	}

	private double createTestDouble() {
		return Math.round(20);
	}
}

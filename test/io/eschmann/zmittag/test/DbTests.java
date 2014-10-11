package io.eschmann.zmittag.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import io.eschmann.zmittag.entities.Group;
import io.eschmann.zmittag.entities.Member;
import io.eschmann.zmittag.entities.Restaurant;
import io.eschmann.zmittag.persistence.ConnectionManager;
import io.eschmann.zmittag.persistence.GroupDao;
import io.eschmann.zmittag.persistence.MemberDao;
import io.eschmann.zmittag.persistence.RestaurantDao;

import java.net.UnknownHostException;
import java.util.Arrays;
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

	@BeforeClass
	public static void beforeClass() throws UnknownHostException {
		connectionManager = new ConnectionManager();
		groupDao = new GroupDao(connectionManager);
//		groupDao.getCollection().drop();
		memberDao = new MemberDao(connectionManager);
//		memberDao.getCollection().drop();
		restaurantDao = new RestaurantDao(connectionManager);
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
		
		Restaurant foundRestaurant = restaurantDao.findOneRestaurant(testRestaurant.getId());
		
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
		testRestaurant.setLatitude(createTestDouble());
		testRestaurant.setLongitude(createTestDouble());
		testRestaurant.addTag(createTestName());
		testRestaurant.setAverageRating(4.0d);
		testRestaurant.setRatingCount(1);
		
		restaurantDao.save(testRestaurant);
		
		restaurantDao.addRatingToRestaurant(testRestaurant.getId(), 2.0d);
		
		Restaurant foundRestaurant = restaurantDao.findOneRestaurant(testRestaurant.getId());
		assertEquals(0, Double.compare(3.0d, foundRestaurant.getAverageRating()));
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
		
		List<Restaurant> foundRestaurants = restaurantDao.findNearestRestaurants(latitude, longitude);
		assertTrue(foundRestaurants.size() > 0);
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

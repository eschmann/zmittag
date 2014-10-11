package io.eschmann.zmittag.test;

import static org.junit.Assert.assertEquals;
import io.eschmann.zmittag.entities.Group;
import io.eschmann.zmittag.persistence.ConnectionManager;
import io.eschmann.zmittag.persistence.GroupDao;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mongodb.morphia.Key;

public class DbTests {
	
	@Test
	public void testShouldSaveAndFindSimpleGroup() throws UnknownHostException {
		ConnectionManager connectionManager = new ConnectionManager();
		GroupDao groupDao = new GroupDao(connectionManager);
		
		Group testGroup = new Group();
		testGroup.setName("Test Group");
		List<String> members = Arrays.asList(new String[]{"one", "two", "three"});
		testGroup.setMembers(members);
		
		groupDao.save(testGroup);
		
		Key<Group> foundKey = groupDao.findOneId();
		assertEquals(testGroup.getId(), foundKey.getId());
	}

}

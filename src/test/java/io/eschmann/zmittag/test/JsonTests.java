package io.eschmann.zmittag.test;

import static org.junit.Assert.assertEquals;
import io.eschmann.zmittag.entities.Group;
import io.eschmann.zmittag.entities.Member;
import io.eschmann.zmittag.service.ServiceHelper;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

public class JsonTests {
	
	@Test
	public void testShouldSerializePairOfGroupAndMember() {
		Group group = new Group();
		group.setName("test group");
		group.setDate(1413040578853l);
		
		Member member = new Member();
		member.setName("test member");
		member.setEmail("member@zmittag.io");
		
		Pair<Group, Member> pair = Pair.of(group, member);
		String json = ServiceHelper.convertToJson(pair);
		
		String expected = "{\"left\":{\"name\":\"test group\",\"date\":1413040578853,\"members\":[],\"tags\":[]},\"right\":{\"name\":\"test member\",\"email\":\"member@zmittag.io\"}}";
		assertEquals(expected, json);
	}

}

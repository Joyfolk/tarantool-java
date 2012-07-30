package org.tarantool.it;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.text.ParseException;

import org.junit.Test;
import org.tarantool.core.ConnectionFactory;
import org.tarantool.core.exception.TarantoolException;
import org.tarantool.facade.Mapping;
import org.tarantool.facade.TarantoolTemplate;
import org.tarantool.facade.User;

public class TestTemplate {

	@Test
	public void testCycle() throws ParseException, MalformedURLException {
		User user = new User();
		ConnectionFactory connectionFactory = new ConnectionFactory();
		TarantoolTemplate<User, Integer> template = new TarantoolTemplate<User, Integer>(0, connectionFactory, new Mapping<User>(User.class, "id", "phone",
				"point", "iq", "height", "lifeFormId", "salary", "birthday", "name", "sign", "male"));
		assertNotNull(template.save(user).insertOrReplaceAndGet());
		try {
			template.save(user).insert();
			fail();
		} catch (TarantoolException ignored) {

		}
		assertEquals(1,template.save(user).replace());
		assertNotNull(template.find(0,"id").condition(1).list());
		assertEquals(user.getPhone()+1L,template.update(123).add("phone", 1).updateAndGet().getPhone());
		assertNull(template.update(1).add("height", -10).updateAndGet());
		
		connectionFactory.free();
		return;
	}
}

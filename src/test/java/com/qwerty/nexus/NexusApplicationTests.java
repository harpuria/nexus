package com.qwerty.nexus;

import com.qwerty.nexus.organization.OrganizationRepository;
import org.jooq.generated.tables.daos.OrganizationDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NexusApplicationTests {

	@Autowired
	OrganizationRepository repository;

	@Test
	void contextLoads() {
	}

	@Test
	void jooqInsertTest_Record(){
		repository.testRecordInsert();
	}

	@Test
	void jooqUpdateTest_Record() {
		repository.testRecordUpdate();
	}

	@Test
	void jooqSelectTest_DAO(){
		repository.testDaoSelect();
	}

}

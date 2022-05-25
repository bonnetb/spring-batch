/*
 * Copyright 2006-2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.batch.item.database.support;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author Thomas Risberg
 * @author Dave Syer
 * @author Michael Minella
 * @author Henning Pöttker
 */
public class H2PagingQueryProviderTests extends AbstractSqlPagingQueryProviderTests {

	public H2PagingQueryProviderTests() {
		pagingQueryProvider = new H2PagingQueryProvider();
	}

	@Test
	@Override
	public void testGenerateFirstPageQuery() {
		String sql = "SELECT id, name, age FROM foo WHERE bar = 1 ORDER BY id ASC FETCH NEXT 100 ROWS ONLY";
		String s = pagingQueryProvider.generateFirstPageQuery(pageSize);
		assertEquals(sql, s);
	}

	@Test
	@Override
	public void testGenerateRemainingPagesQuery() {
		String sql = "SELECT id, name, age FROM foo WHERE (bar = 1) AND ((id > ?)) "
				+ "ORDER BY id ASC FETCH NEXT 100 ROWS ONLY";
		String s = pagingQueryProvider.generateRemainingPagesQuery(pageSize);
		assertEquals(sql, s);
	}

	@Test
	@Override
	public void testGenerateJumpToItemQuery() {
		String sql = "SELECT id FROM foo WHERE bar = 1 ORDER BY id ASC OFFSET 99 ROWS FETCH NEXT 1 ROWS ONLY";
		String s = pagingQueryProvider.generateJumpToItemQuery(145, pageSize);
		assertEquals(sql, s);
	}

	@Test
	@Override
	public void testGenerateJumpToItemQueryForFirstPage() {
		String sql = "SELECT id FROM foo WHERE bar = 1 ORDER BY id ASC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
		String s = pagingQueryProvider.generateJumpToItemQuery(45, pageSize);
		assertEquals(sql, s);
	}

	@Override
	@Test
	public void testGenerateFirstPageQueryWithGroupBy() {
		pagingQueryProvider.setGroupClause("dep");
		String sql = "SELECT id, name, age FROM foo WHERE bar = 1 GROUP BY dep ORDER BY id ASC FETCH NEXT 100 ROWS ONLY";
		String s = pagingQueryProvider.generateFirstPageQuery(pageSize);
		assertEquals(sql, s);
	}

	@Override
	@Test
	public void testGenerateRemainingPagesQueryWithGroupBy() {
		pagingQueryProvider.setGroupClause("dep");
		String sql = "SELECT id, name, age FROM foo WHERE (bar = 1) AND ((id > ?)) GROUP BY dep "
				+ "ORDER BY id ASC FETCH NEXT 100 ROWS ONLY";
		String s = pagingQueryProvider.generateRemainingPagesQuery(pageSize);
		assertEquals(sql, s);
	}

	@Override
	@Test
	public void testGenerateJumpToItemQueryWithGroupBy() {
		pagingQueryProvider.setGroupClause("dep");
		String sql = "SELECT id FROM foo WHERE bar = 1 GROUP BY dep ORDER BY id ASC OFFSET 99 ROWS FETCH NEXT 1 ROWS ONLY";
		String s = pagingQueryProvider.generateJumpToItemQuery(145, pageSize);
		assertEquals(sql, s);
	}

	@Override
	@Test
	public void testGenerateJumpToItemQueryForFirstPageWithGroupBy() {
		pagingQueryProvider.setGroupClause("dep");
		String sql = "SELECT id FROM foo WHERE bar = 1 GROUP BY dep ORDER BY id ASC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
		String s = pagingQueryProvider.generateJumpToItemQuery(45, pageSize);
		assertEquals(sql, s);
	}

	@Override
	public String getFirstPageSqlWithMultipleSortKeys() {
		return "SELECT id, name, age FROM foo WHERE bar = 1 ORDER BY name ASC, id DESC FETCH NEXT 100 ROWS ONLY";
	}

	@Override
	public String getRemainingSqlWithMultipleSortKeys() {
		return "SELECT id, name, age FROM foo WHERE (bar = 1) AND ((name > ?) OR (name = ? AND id < ?)) "
				+ "ORDER BY name ASC, id DESC FETCH NEXT 100 ROWS ONLY";
	}

	@Override
	public String getJumpToItemQueryWithMultipleSortKeys() {
		return "SELECT name, id FROM foo WHERE bar = 1 ORDER BY name ASC, id DESC OFFSET 99 ROWS FETCH NEXT 1 ROWS ONLY";
	}

	@Override
	public String getJumpToItemQueryForFirstPageWithMultipleSortKeys() {
		return "SELECT name, id FROM foo WHERE bar = 1 ORDER BY name ASC, id DESC OFFSET 0 ROWS FETCH NEXT 1 ROWS ONLY";
	}

}

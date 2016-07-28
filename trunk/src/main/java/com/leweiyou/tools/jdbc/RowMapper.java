package com.leweiyou.tools.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 行映射
 * @author Zhangweican
 *
 * @param <T>
 */
public interface RowMapper<T> {

	public abstract T mapRow(ResultSet rs) throws SQLException;

}
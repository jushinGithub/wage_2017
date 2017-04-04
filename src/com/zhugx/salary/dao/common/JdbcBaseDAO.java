package  com.zhugx.salary.dao.common;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcDaoSupport;

@SuppressWarnings("all")
public class JdbcBaseDAO extends SimpleJdbcDaoSupport  {


	/**
	 * 获取列表数据
	 * @param sql
	 * @param entityClass
	 * @return
	 */
	
	public List queryForList(String sql, Class entityClass){
		return this.getSimpleJdbcTemplate().query(sql, new BeanPropertyRowMapper(entityClass));
	}
	
	/**
	 * 获取单个数据
	 * @param sql
	 * @param entityClass
	 * @return
	 */
	public List queryForObject(String sql, Class entityClass){
		return this.getSimpleJdbcTemplate().queryForObject(sql,  
                ParameterizedBeanPropertyRowMapper.newInstance(entityClass)); 
	}

}

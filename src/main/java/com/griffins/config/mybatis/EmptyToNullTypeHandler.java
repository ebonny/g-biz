package com.griffins.config.mybatis;

import com.griffins.common.util.StringUtil;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 공백을 무조건 NULL 로 처리하여 쿼리에서 A IS NULL 은 FALSE 인데 A = '' 는 TRUE 가 되는,,
 * 개발자한테 빅엿을 먹이는 상황을 예방한다. (참고로 오라클은 이런 빅엿을 주지 않는다)
 * ===============================================
 *
 * @author 이재철
 * @since 2017-04-28
 * *
 * 수정자          수정일          수정내용
 * -------------  -------------  -----------------
 * *
 * ===============================================
 * Copyright (C) by ESMP All right reserved.
 */
@MappedJdbcTypes({JdbcType.VARCHAR, JdbcType.LONGVARCHAR})
public class EmptyToNullTypeHandler implements TypeHandler<String> {

    private String emptyToNull(String value) {
        if (value.equals(""))
            return null;
        else
            return value;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            if (jdbcType == null) {
                throw new TypeException("JDBC requires that the JdbcType must be specified for all nullable parameters.");
            }
            try {
                ps.setNull(i, jdbcType.TYPE_CODE);
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . " +
                        "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. " +
                        "Cause: " + e, e);
            }
        } else {
            ps.setString(i, emptyToNull(parameter));
        }
    }

    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return StringUtil.nvl(rs.getString(columnName));
    }

    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        return StringUtil.nvl(rs.getString(columnIndex));
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return StringUtil.nvl(cs.getString(columnIndex));
    }
}

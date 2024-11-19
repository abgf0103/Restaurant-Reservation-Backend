package kr.re.kh.handler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

// extends BaseTypeHandler<LocalDateTime>
public class LocalDateTimeTypeHandler  {

//    @Override
//    public void setNonNullParameter(PreparedStatement ps, int i, LocalDateTime parameter, JdbcType jdbcType) throws SQLException {
//        if (parameter != null) {
//            ps.setTimestamp(i, Timestamp.valueOf(parameter));  // LocalDateTime -> Timestamp
//        } else {
//            ps.setNull(i, java.sql.Types.TIMESTAMP);
//        }
//    }
//
//    @Override
//    public LocalDateTime getNullableResult(ResultSet rs, String columnName) throws SQLException {
//        Timestamp timestamp = rs.getTimestamp(columnName);
//        return timestamp != null ? timestamp.toLocalDateTime() : null;
//    }
//
//    @Override
//    public LocalDateTime getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
//        Timestamp timestamp = rs.getTimestamp(columnIndex);
//        return timestamp != null ? timestamp.toLocalDateTime() : null;
//    }
//
//    @Override
//    public LocalDateTime getNullableResult(java.sql.CallableStatement cs, int columnIndex) throws SQLException {
//        Timestamp timestamp = cs.getTimestamp(columnIndex);
//        return timestamp != null ? timestamp.toLocalDateTime() : null;
//    }
}

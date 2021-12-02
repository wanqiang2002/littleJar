package com.wq.haloArticle;

import com.wq.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

public class ArticleDao  {


    /**
     * 查询全部稿件
     *
     * @return
     * @throws SQLException
     */
    public List<AtricleVo> query() throws SQLException
    {
        /*List<AtricleVo> goddessList = new ArrayList<AtricleVo>();

        // 获得数据库连接
        Connection conn = DBUtil.getConnection();

        StringBuilder sb = new StringBuilder();
        sb.append("select id,name,mobie,email,address from goddess");

        // 通过数据库的连接操作数据库，实现增删改查
        PreparedStatement ptmt = conn.prepareStatement(sb.toString());

        ResultSet rs = ptmt.executeQuery();

        AtricleVo goddess = null;

        while (rs.next())
        {
            goddess = new AtricleVo();
            goddess.setId(rs.getInt("id"));
            goddess.setName(rs.getString("name"));
            goddess.setMobie(rs.getString("mobie"));
            goddess.setEmail(rs.getString("email"));
            goddess.setAddress(rs.getString("address"));

            goddessList.add(goddess);
        }
        return goddessList;*/
        return  null;
    }

    /**
     * 查询单个稿件
     *
     * @return
     * @throws SQLException
     */
    public AtricleVo queryById(Integer id) throws SQLException
    {
        /*AtricleVo g = null;

        Connection conn = DBUtil.getConnection();

        String sql = "" + " select * from imooc_goddess " + " where id=? ";

        PreparedStatement ptmt = conn.prepareStatement(sql);

        ptmt.setInt(1, id);

        ResultSet rs = ptmt.executeQuery();

        while (rs.next())
        {
            g = new AtricleVo();
            g.setId(rs.getInt("id"));
            g.setName(rs.getString("name"));
            g.setMobie(rs.getString("mobie"));
            g.setEmail(rs.getString("email"));
            g.setAddress(rs.getString("address"));
        }

        return g;*/
        return null;
    }

    /**
     * 添加稿件
     *
     * @throws SQLException
     */
    public void addAtricleVo(AtricleVo atricleVo){
        // 获得数据库连接
        Connection conn = DBUtil.getConnection();
        String sql = "insert into posts(editor_type,create_time,update_time,edit_time,slug,format_content,original_content,title,status)" +
                " values('0',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?,'',?,?,'0')";

        PreparedStatement ptmt = null;
        try {
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, UUID.randomUUID().toString());
            ptmt.setString(2, atricleVo.getContent());
            ptmt.setString(3, atricleVo.getTitle());
            ptmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.closeConnection(ptmt,conn);
        }
    }


    /**
     * 修改稿件资料
     *
     * @throws SQLException
     */
    public void updateAtricleVo(AtricleVo goddess) throws SQLException
    {
       /* Connection conn = DBUtil.getConnection();

        String sql = "update goddess set name=?,mobie=?,email=?,address=? where id=?";

        PreparedStatement ptmt = conn.prepareStatement(sql);

        ptmt.setString(1, goddess.getName());
        ptmt.setString(2, goddess.getMobie());
        ptmt.setString(3, goddess.getEmail());
        ptmt.setString(4, goddess.getAddress());

        ptmt.execute();*/
    }

    /**
     * 删除稿件
     *
     * @throws SQLException
     */
    public void deleteAtricleVo(Integer id) throws SQLException
    {
        Connection conn = DBUtil.getConnection();

        String sql = "delete from goddess where id=?";

        PreparedStatement ptmt = conn.prepareStatement(sql);

        ptmt.setInt(1, id);

        ptmt.execute();
    }
}

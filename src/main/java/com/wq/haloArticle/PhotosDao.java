package com.wq.haloArticle;

import com.wq.utils.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PhotosDao {

    /**
     * 添加图片
     *
     * @throws SQLException
     */
    public void addPhotosVo(PhotosVo photosVo){
        // 获得数据库连接
        Connection conn = DBUtil.getConnection();
        //保存为草稿
        String sql = "insert into photos(create_time,update_time,take_time,name,thumbnail,url,team)" +
                " values(CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,?,?,?,'必应壁纸-每日美图')";

        PreparedStatement ptmt = null;
        try {
            ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, photosVo.getName());
            ptmt.setString(2, photosVo.getThumbnail());
            ptmt.setString(3, photosVo.getUrl());
            ptmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            DBUtil.closeConnection(ptmt,conn);
        }
    }
}

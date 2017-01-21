package com.xj.images;

import com.xj.images.data.BaiduDataSourceImpl;
import com.xj.images.data.DataSource;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void getDataUseBaiduDataSourceImpl(){
        DataSource dataSource = new BaiduDataSourceImpl();
        String data = dataSource.getData("美女", 2);
        assertNotNull(data);
    }
}
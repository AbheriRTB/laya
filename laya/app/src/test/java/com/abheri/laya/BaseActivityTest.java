package com.abheri.laya;

import org.junit.runner.RunWith;
import com.abheri.laya.BuildConfig;
import com.abheri.laya.R;
import com.abheri.laya.activities.BaseActivity;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
/**
 * Created by prasanna.ramaswamy on 23/03/18.
 */

public class BaseActivityTest {
    private BaseActivity activity;

    @Before
    public void setUp() throws Exception
    {
        activity = Robolectric.buildActivity( BaseActivity.class )
                .create(null)
                .resume()
                .get();
    }

    @Test
    public void shouldNotBeNull() throws Exception
    {
        assertNotNull( activity );
    }

}

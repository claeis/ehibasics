package ch.ehi.basics.settings;

import org.junit.Assert;
import org.junit.Test;

public class SettingsTest {
    @Test
    public void spaceAsValue()
    {
        Settings settings=new Settings();
        final String VALUE=" ";
        final String NAME="test";
        settings.setValue(NAME,VALUE);
        Assert.assertEquals(VALUE,settings.getValue(NAME));
    }
    @Test
    public void spaceAsTransientValue()
    {
        Settings settings=new Settings();
        final String VALUE=" ";
        final String NAME="test";
        settings.setTransientValue(NAME,VALUE);
        Assert.assertEquals(VALUE,settings.getTransientValue(NAME));
    }

}

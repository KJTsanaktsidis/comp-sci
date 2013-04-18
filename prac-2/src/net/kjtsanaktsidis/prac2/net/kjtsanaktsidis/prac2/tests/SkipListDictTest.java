package net.kjtsanaktsidis.prac2.net.kjtsanaktsidis.prac2.tests;

import net.kjtsanaktsidis.prac2.SkipListDict;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class SkipListDictTest {

    private Map<Integer, String> getPreparedDict()
    {
        Map<Integer, String> dict = new SkipListDict<>();
        dict.put(4, "four");
        dict.put(1, "one");
        dict.put(9, "nine");
        dict.put(5, "five");
        dict.put(0, "zero");
        return dict;
    }

    @Test
    public void canPutItems()
    {
        Map<Integer, String> dict = new SkipListDict<>();
        dict.put(0, "zero");
        dict.put(1, "one");
    }

    @Test
    public void canGetItems()
    {
        Map<Integer, String> dict = getPreparedDict();
        Assert.assertEquals(dict.get(0), "zero");
        Assert.assertEquals(dict.get(1), "one");
        Assert.assertEquals(dict.get(4), "four");
        Assert.assertEquals(dict.get(5), "five");
        Assert.assertEquals(dict.get(9), "nine");
    }

    @Test
    public void dontGetMissingItems()
    {
        Map<Integer, String> dict = getPreparedDict();
        Assert.assertEquals(dict.get(-9), null);
        Assert.assertEquals(dict.get(82), null);
    }

    @Test
    public void canCheckContainsKey()
    {
        Map<Integer, String> dict = getPreparedDict();
        Assert.assertTrue(dict.containsKey(0));
        Assert.assertTrue(dict.containsKey(1));
        Assert.assertTrue(dict.containsKey(5));

        Assert.assertFalse(dict.containsKey(-9));
        Assert.assertFalse(dict.containsKey(83));
    }

    @Test
    public void canCheckContainsValue()
    {
        Map<Integer, String> dict = getPreparedDict();
        Assert.assertTrue(dict.containsValue("zero"));
        Assert.assertTrue(dict.containsValue("one"));
        Assert.assertTrue(dict.containsValue("five"));

        Assert.assertFalse(dict.containsValue("minus nine"));
        Assert.assertFalse(dict.containsValue("eighty three"));
    }

    @Test
    public void canCheckCount()
    {
        Map<Integer, String> dict = new SkipListDict<>();
        Assert.assertEquals(dict.size(), 0);

        dict.put(0, "zero");
        dict.put(8, "eight");
        Assert.assertEquals(dict.size(), 2);

        dict = getPreparedDict();
        Assert.assertEquals(dict.size(), 5);
    }

    @Test
    public void canClearAndIsEmpty()
    {
        Map<Integer, String> dict = getPreparedDict();
        Assert.assertEquals(dict.size(), 5);
        Assert.assertFalse(dict.isEmpty());
        dict.clear();
        Assert.assertEquals(dict.size(), 0);
        Assert.assertTrue(dict.isEmpty());
    }

    @Test
    public void canRemove()
    {
        Map<Integer, String> dict = getPreparedDict();
        Assert.assertTrue(dict.containsKey(0));
        dict.remove(0);
        Assert.assertFalse(dict.containsKey(0));
        dict.remove(1);
        dict.remove(4);
        dict.remove(5);
        dict.remove(9);
        Assert.assertTrue(dict.isEmpty());
    }

}

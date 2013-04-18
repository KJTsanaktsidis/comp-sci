package net.kjtsanaktsidis.collections.tests;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import net.kjtsanaktsidis.collections.AVLTree;

public class AVLTreeTests {

    @Test
    public void CanAdd()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(1, "value");
    }

    @Test
    public void CanRetrieve()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(4, "four");
        tree.put(1, "one");
        tree.put(9, "nine");
        tree.put(5, "five");
        tree.put(0, "zero");

        Assert.assertEquals(tree.get(0), "zero");
        Assert.assertEquals(tree.get(1), "one");
        Assert.assertEquals(tree.get(4), "four");
        Assert.assertEquals(tree.get(5), "five");
        Assert.assertEquals(tree.get(9), "nine");
    }

    @Test
    public void CanCount()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        Assert.assertEquals(tree.size(), 0);
        tree.put(4, "four");
        tree.put(1, "one");
        tree.put(9, "nine");
        tree.put(5, "five");
        tree.put(0, "zero");
        Assert.assertEquals(tree.size(), 5);
    }

    @Test
    public void CanCheckContainsKey()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(4, "four");
        tree.put(1, "one");
        tree.put(9, "nine");
        tree.put(5, "five");
        tree.put(0, "zero");

        Assert.assertTrue(tree.containsKey(4));
        Assert.assertTrue(tree.containsKey(9));
        Assert.assertFalse(tree.containsKey(-1));
        Assert.assertFalse(tree.containsKey(88));
    }

    @Test(expected = ClassCastException.class)
    public void CantCheckForWrongTypeKey()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.containsKey("gheed");
    }

    @Test(expected = NullPointerException.class)
    public void CantInsertNullKey()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(null, "refl");
    }

    @Test
    public void CanCheckContainsValue()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(4, "four");
        tree.put(1, "one");
        tree.put(9, "nine");
        tree.put(5, "five");
        tree.put(0, "zero");

        Assert.assertTrue(tree.containsValue("four"));
        Assert.assertTrue(tree.containsValue("nine"));
        Assert.assertFalse(tree.containsValue("sixty-nine"));
    }

    @Test(expected = ClassCastException.class)
    public void CantCheckForWrongTypeValue()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.containsValue(new ArrayList<Integer>());
    }

    @Test
    public void CanGetEntrySet()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(4, "four");
        tree.put(1, "one");
        tree.put(9, "nine");
        tree.put(5, "five");
        tree.put(0, "zero");

        Set<Map.Entry<Integer, String>> set = tree.entrySet();
        //Should be in ascending key order
        Iterator<Map.Entry<Integer, String>> it = set.iterator();
        Map.Entry<Integer, String> zero = it.next();
        Map.Entry<Integer, String> one = it.next();
        Map.Entry<Integer, String> four = it.next();
        Assert.assertEquals((int)zero.getKey(), 0);
        Assert.assertEquals(zero.getValue(), "zero");
        Assert.assertEquals((int)one.getKey(), 1);
        Assert.assertEquals(one.getValue(), "one");
        Assert.assertEquals((int)four.getKey(), 4);
        Assert.assertEquals(four.getValue(), "four");
    }

    @Test
    public void CanModifyEntrySet()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(4, "four");
        tree.put(1, "one");
        tree.put(9, "nine");
        tree.put(5, "five");
        tree.put(0, "zero");

        Set<Map.Entry<Integer, String>> set = tree.entrySet();
        Iterator<Map.Entry<Integer, String>> it = set.iterator();
        Map.Entry<Integer, String> zero = it.next();
        zero.setValue("notzero");
        Assert.assertEquals(tree.get(0), "notzero");
    }

    @Test
    public void CanCheckEquality()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(4, "four");
        tree.put(1, "one");
        tree.put(9, "nine");
        tree.put(5, "five");
        tree.put(0, "zero");

        HashMap<Integer, String> other = new HashMap<>();
        other.put(4, "four");
        other.put(1, "one");
        other.put(9, "nine");
        other.put(5, "five");
        Assert.assertFalse(tree.equals(other));
        other.put(0, "zero");
        Assert.assertTrue(tree.equals(other));

        HashMap<Integer, Integer> wrongtype = new HashMap<>();
        Assert.assertFalse(tree.equals(wrongtype));
    }

    @Test
    public void CanCheckIsEmpty()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        Assert.assertTrue(tree.isEmpty());
        tree.put(1, "lol");
        Assert.assertFalse(tree.isEmpty());
        tree.remove(1);
        Assert.assertTrue(tree.isEmpty());
    }

    @Test
    public void CanGetKeyset()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(4, "four");
        tree.put(1, "one");
        tree.put(9, "nine");
        tree.put(5, "five");
        tree.put(0, "zero");

        Set<Integer> keys = tree.keySet();
        Iterator<Integer> it = keys.iterator();
        Assert.assertEquals((int)it.next(), 0);
        Assert.assertEquals((int)it.next(), 1);
        Assert.assertEquals((int)it.next(), 4);
        Assert.assertEquals((int)it.next(), 5);
        Assert.assertEquals((int)it.next(), 9);
        Assert.assertFalse(it.hasNext());

        keys.remove(4);
        Assert.assertFalse(tree.containsKey(4));
    }

    @Test
    public void CanPutAll()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        Map<Integer, String> sourceMap = new HashMap<>();
        sourceMap.put(1, "one");
        sourceMap.put(2, "two");
        sourceMap.put(3, "three");
        tree.putAll(sourceMap);

        Assert.assertEquals(tree.get(1), "one");
        Assert.assertEquals(tree.size(), 3);
    }

    @Test
    public void CanRemove()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(4, "four");
        tree.put(1, "one");
        tree.put(9, "nine");
        tree.put(5, "five");
        tree.put(0, "zero");

        Assert.assertEquals(tree.size(), 5);
        tree.remove(0);
        Assert.assertEquals(tree.size(), 4);
        Assert.assertFalse(tree.containsKey(0));
        tree.remove(4);
        tree.remove(1);
        tree.remove(9);
        tree.remove(5);
        Assert.assertEquals(tree.size(), 0);

        //can return stuff into it?
        tree.put(66, "sixsix");
        Assert.assertEquals(66, "sixsix");
    }

    @Test
    public void CanGetValues()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(4, "four");
        tree.put(1, "one");
        tree.put(9, "nine");
        tree.put(5, "five");
        tree.put(0, "zero");

        Collection<String> values = tree.values();
        Assert.assertEquals(values.size(), 5);
        Assert.assertTrue(values.contains("one"));
        Assert.assertTrue(values.contains("four"));
        Assert.assertTrue(values.contains("zero"));
        Assert.assertFalse(values.contains("minusone"));

        Iterator<String> it = values.iterator();
        Assert.assertEquals(it.next(), "zero");
        Assert.assertEquals(it.next(), "one");
        Assert.assertEquals(it.next(), "four");
        Assert.assertEquals(it.next(), "five");
        Assert.assertEquals(it.next(), "nine");
        Assert.assertFalse(it.hasNext());

        values.remove("zero");
        Assert.assertFalse(tree.containsKey(0));
        Assert.assertFalse(tree.containsValue("zero"));
    }

    @Test
    public void CanGetFirstLastKey()
    {
        SortedMap<Integer, String> tree = new AVLTree<Integer, String>();
        tree.put(4, "four");
        tree.put(1, "one");
        tree.put(9, "nine");
        tree.put(5, "five");
        tree.put(0, "zero");

        Assert.assertEquals((int)tree.firstKey(), 0);
        tree.remove(0);
        Assert.assertEquals((int)tree.firstKey(), 1);
        Assert.assertEquals((int)tree.lastKey(), 9);
        tree.remove(9);
        Assert.assertEquals((int)tree.lastKey(), 5);
    }

    @Test

}

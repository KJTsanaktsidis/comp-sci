package net.kjtsanaktsidis.prac2;

import java.lang.reflect.Array;
import java.util.*;

public class SkipListDict<K extends Comparable<? super K>, T> implements Map<K, T> {

    private class Tower {

        private Tower flinks[];
        private Tower blinks[];
        private K key;
        private T value;
        private boolean isSentinel;

        @SuppressWarnings({"unchecked"})
        public Tower(K key, T value, int height) {
            this.flinks = (Tower[]) Array.newInstance(Tower.class, height);
            this.blinks = (Tower[]) Array.newInstance(Tower.class, height);
            this.key = key;
            this.value = value;
            this.isSentinel = false;
        }

        @SuppressWarnings({"unchecked"})
        public Tower(int height, boolean isSentinel) {
            this.flinks = (Tower[]) Array.newInstance(Tower.class, height);
            this.blinks = (Tower[]) Array.newInstance(Tower.class, height);
            this.isSentinel = isSentinel;
        }

        @SuppressWarnings({"unchecked"})
        private void grow(int newHeight) {
            Tower[] newflinks = (Tower[]) Array.newInstance(Tower.class, newHeight);
            Tower[] newblinks = (Tower[]) Array.newInstance(Tower.class, newHeight);
            System.arraycopy(this.flinks, 0, newflinks, 0, this.flinks.length);
            System.arraycopy(this.blinks, 0, newblinks, 0, this.blinks.length);
            this.flinks = newflinks;
            this.blinks = newblinks;
        }
    }

    private Tower leftSentinel;
    private Tower rightSentinel;
    private int count;

    public SkipListDict()
    {
        //Set up L/Rsentinels
        this.leftSentinel = new Tower(1, true);
        this.rightSentinel = new Tower(1, true);
        this.leftSentinel.flinks[0] = this.rightSentinel;
        this.rightSentinel.blinks[0] = this.leftSentinel;

        this.count = 0;
    }

    private Tower findTowerOrLeft(K key) {
        return this.findTowerOrLeft(key, this.leftSentinel);
    }

    private Tower findTowerOrNull(K key) {
        Tower lmostOrVal = this.findTowerOrLeft(key);
        if (!lmostOrVal.isSentinel && lmostOrVal.key.equals(key)) {
            return lmostOrVal;
        }
        else {
            return null;
        }
    }

    private Tower findTowerOrLeft(K key, Tower curNode) {
        //Check if key is us first, to skip the loop
        if (!curNode.isSentinel && curNode.key.equals(key)) {
            return curNode;
        }

        //Search from top to bottom of the links in this node to see if It's smaller than key
        for (int i = curNode.flinks.length - 1; i >= 0; i--) {
            if (!curNode.flinks[i].isSentinel) {
                if (curNode.flinks[i].key.compareTo(key) <= 0) {
                    //this node is less than (or equal to) our key, use it as search base
                    return this.findTowerOrLeft(key, curNode.flinks[i]);
                }
            }
        }
        //If we got here, we must be the largest node that is smaller than key, and key is not found
        return curNode;
    }

    @SuppressWarnings({"unchecked"})
    private Tower makeTower(Tower leftNode, K targetKey) {
        //create a tower to the right of leftNode
        int height = PoissonGenerator.generate();
        Tower nTower = new Tower(height, false);

        //fist things first- do we need to boost up the sentinel tower height?
        if (height >= this.leftSentinel.flinks.length) {
            int oldheight = this.leftSentinel.flinks.length;
            this.leftSentinel.grow(height + 1);
            this.rightSentinel.grow(height + 1);
            for (int i = oldheight; i < height + 1; i++) {
                this.leftSentinel.flinks[i] = this.rightSentinel;
                this.rightSentinel.blinks[i] = this.leftSentinel;
            }
        }

        //Now we need to join up all the links on the new tower.
        //to do this, we want:
        //      Every tower to the left of the new node that connects to a tower on the right of the new node at a level
        //      <= the new node height
        //can do this by following each link from leftsentinel to rightsentinel at a level <= height
        Tower[] updateTargets = (Tower[])Array.newInstance(Tower.class, height);
        Tower lSearchNode = this.leftSentinel;
        for (int i = height-1; i >= 0; i--) {
            //get the tower we will need to update on this level
            //"keep looping while i havn't hit something to the right of where this node should be
            while (!lSearchNode.flinks[i].isSentinel &&
                    lSearchNode.flinks[i].key.compareTo(targetKey) < 0) {
                lSearchNode = lSearchNode.flinks[i];
            }
            updateTargets[i] = lSearchNode;
        }

        //We now have an array of update targets on our LHS we need to make point to us
        for (int i = 0; i < height; i++) {
            nTower.flinks[i] = updateTargets[i].flinks[i];
            updateTargets[i].flinks[i].blinks[i] = nTower;
            nTower.blinks[i] = updateTargets[i];
            updateTargets[i].flinks[i] = nTower;
        }

        return nTower;
    }

    @Override
    public T put(K key, T value) {
        //find the insertion point
        Tower insertionPoint = this.findTowerOrLeft(key);
        //Do we need to insert, or just update?
        Tower keyNode;
        T oldval;
        if (insertionPoint != this.leftSentinel && insertionPoint.key.equals(key)) {
            keyNode = insertionPoint;
            oldval = keyNode.value;
        }
        else {
            //need to create + link
            keyNode = makeTower(insertionPoint, key);
            oldval = null;
            this.count++;
        }

        keyNode.key = key;
        keyNode.value = value;
        return oldval;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public T get(Object key) {
        Tower searchResult = this.findTowerOrNull((K)key);
        if (searchResult == null) {
            return null;
        }
        else {
            return searchResult.value;
        }
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public T remove(Object key) {
        Tower searchResult = this.findTowerOrNull((K)key);
        if (searchResult == null) {
            return null;
        }
        else {
            //update the flinks + blinks on search result
            for (int i = 0; i < searchResult.flinks.length; i++) {
                Tower left = searchResult.blinks[i];
                Tower right = searchResult.flinks[i];
                left.flinks[i] = right;
                right.blinks[i] = left;
            }
            this.count--;
            return searchResult.value;
        }
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override
    @SuppressWarnings({"unchecked"})
    public boolean containsKey(Object key) {
        return findTowerOrNull((K)key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Tower cTower = this.leftSentinel;
        while (!cTower.flinks[0].isSentinel &&
                !cTower.flinks[0].value.equals(value)) {
            cTower = cTower.flinks[0];
        }
        //why'd we stop?
        return !cTower.flinks[0].isSentinel;
    }

    @Override
    public void clear() {
        //just link up the left and right snetinels; everything else will get GC'd
        for (int i = 0; i < this.leftSentinel.flinks.length; i++) {
            this.leftSentinel.flinks[i] = this.rightSentinel;
            this.rightSentinel.blinks[i] = this.leftSentinel;
        }
        this.count = 0;
    }

    @Override
    public void putAll(Map<? extends K, ? extends T> m) {
        for (Entry<? extends K, ? extends T> ent : m.entrySet()) {
            this.put(ent.getKey(), ent.getValue());
        }
    }

    @Override
    public Set<K> keySet() {
        Set<K> rSet = new HashSet<>();
        Tower cTower = this.leftSentinel;
        while (!cTower.flinks[0].isSentinel) {
            rSet.add(cTower.flinks[0].key);
            cTower = cTower.flinks[0];
        }
        return rSet;
    }

    @Override
    public Collection<T> values() {
        ArrayList<T> rList = new ArrayList<>();
        Tower cTower = this.leftSentinel;
        while (!cTower.flinks[0].isSentinel) {
            rList.add(cTower.flinks[0].value);
            cTower = cTower.flinks[0];
        }
        return rList;
    }

    @Override
    public Set<Entry<K, T>> entrySet() {
        Set<Entry<K,T>> rSet = new HashSet<>();
        Tower cTower = this.leftSentinel;
        while (!cTower.flinks[0].isSentinel) {
            Entry<K, T> e = new AbstractMap.SimpleImmutableEntry<>
                    (cTower.flinks[0].key, cTower.flinks[0].value);
            rSet.add(e);
            cTower = cTower.flinks[0];
        }
        return rSet;
    }
}

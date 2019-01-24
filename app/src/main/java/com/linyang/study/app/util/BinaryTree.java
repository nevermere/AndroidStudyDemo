package com.linyang.study.app.util;

/**
 * 描述:
 * Created by fzJiang on 2018/11/27 下午 4:35 星期二
 */
public class BinaryTree {

    private BNode mRoot;// g根节点

    public BinaryTree() {
        mRoot = null;
    }


    /**
     * 二叉搜索树查找的时间复杂度为O(logN)
     *
     * @param key
     * @return
     */
    public BNode findNode(int key) {
        if (mRoot == null) {
            return null;
        }
        BNode current = mRoot;
        while (key != current.key) {
            if (key < current.key) {
                current = current.leftChild;
            } else {
                current = current.rightChild;
            }
        }
        return current;
    }

    /**
     * 插入节点
     *
     * @param key
     * @param data
     */
    public void insertNode(int key, double data) {
        BNode newNode = new BNode();
        newNode.key = key;
        newNode.data = data;

        // 根节点为空，则直接插入作为根节点
        if (mRoot == null) {
            mRoot = newNode;
        } else {
            BNode current = mRoot;
            BNode parent;
            while (true) {
                parent = current;
                if (key < current.key) {
                    current = current.leftChild;
                    if (current == null) {
                        parent.leftChild = newNode;
                        newNode.parent = parent;
                        return;
                    }
                } else {
                    current = current.rightChild;
                    if (current == null) {
                        parent.rightChild = newNode;
                        newNode.parent = parent;
                        return;
                    }
                }
            }
        }
    }


    /**
     * 删除节点在二叉树中是最复杂的，主要有三种情况：
     * 1. 该节点没有子节点（简单）
     * 2. 该节点有一个子节点（还行）
     * 3. 该节点有两个子节点（复杂）
     * 删除节点的时间复杂度为O(logN)
     *
     * @param key
     * @return
     */
    public boolean deleteNode(int key) {
        BNode current = mRoot;
        boolean isLeftNode = true;

        if (current == null) {
            return false;
        }

        // 查找需要删除的节点
        while (key != current.key) {
            if (key < current.key) {
                isLeftNode = true;
                current = current.leftChild;
            } else {
                isLeftNode = false;
                current = current.rightChild;
            }
            if (current == null) {
                return false;
            }
        }

        boolean isDeleted = false;
        // 开始删除
        if (current.leftChild == null && current.rightChild == null) {
            // 该节点没有子节点
            if (current == mRoot) {
                mRoot = null;
                isDeleted = true;
            } else {
                if (isLeftNode) {
                    current.parent.leftChild = null;
                } else {
                    current.parent.rightChild = null;
                }
                isDeleted = true;
            }
        } else if (current.leftChild != null && current.rightChild != null) {
            // 该节点有两个子节点



        } else {
            // 该节点有一个子节点


        }
        return isDeleted;
    }

    private class BNode {
        private int key;
        private double data;
        private BNode parent;
        private BNode leftChild;
        private BNode rightChild;
    }
}

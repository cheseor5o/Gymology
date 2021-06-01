package util;

import java.util.NoSuchElementException;

/**
 * 一个用于保存浏览过的scene的特殊双向链表，其默认最大保存数为50
 * @param <T> 元素，通常为 javafx.scene.Parent
 * @see javafx.scene.Parent
 * @author Dong
 * @since May 29, 2021
 */
public class SceneList<T> {
    private Node<T> first;
    private Node<T> last;
    private Node<T> current;
    final int maxSize;
    private int size = 0;

    public SceneList() {
        maxSize = 50;
    }

    public SceneList(int maxSize) {
        if (maxSize == 0) throw new UnsupportedOperationException("The size of sceneList cannot be 0!");
        this.maxSize = maxSize;
    }

    private static class Node<T>{
        T data;
        Node<T> previous;
        Node<T> next;
        Node(Node<T> previous, T data, Node<T> next) {
            this.data = data;
            this.next = next;
            this.previous = previous;
        }
    }


    public T currentScene() {
        return current == null ? null : current.data;
    }

    /**
     * 获取前一个scene
     * @return 前一个scene
     */
    public T preScene(){
        Node<T> pre = current.previous;
        if (pre == null){
            return null;
        }else {
            current = pre;
            return current.data;
        }
    }

    /**
     * 获取后一个scene
     * @return 后一个scene
     */
    public T nextScene(){
        Node<T> next = current.next;
        if (next == null){
            return null;
        }else {
            current = next;
            return current.data;
        }
    }

    /**
     * 添加元素到链表头部
     * @param data 元素
     */
    private void addFirst(T data){
        final Node<T> f = first;
        Node<T> newNode = new Node<>(null, data, f);
        first = newNode;
        if (f==null){
            last = newNode;
        }else {
            f.previous = newNode;
        }
        ++size;
    }

    /**
     * 根据current scene自适应添加元素
     * @param data 元素
     */
    public void add(T data){
        if (size < maxSize){
            if (current == last){
                addLast(data);
            }else {
                current.next.data = data;
                current = current.next;
            }
        }else {//保存的scene等于maxsize了
            if (current == last){
                removeFirst();
                addLast(data);
            }else {
                current.next.data = data;
                current = current.next;
            }
        }
        
    }
    
    private void addLast(T data){
        final Node<T> l = last;
        Node<T> newNode = new Node<>(l, data, null);
        last = newNode;
        if (l==null){
            first = newNode;
        }else {
            l.next = newNode;
        }
        current = last;//添加后，current等于这一个scene
        ++size;
    }

    /**
     * 删除链表头部元素
     * @return 该元素
     */
    private T removeFirst() {
        final Node<T> f = first;
        if (f == null) throw new NoSuchElementException();
        T data = f.data;
        final Node<T> next = f.next;
        f.next = null;
        f.data = null;
        if (next == null){
            current = last = null;
        }else {
            next.previous = null;
        }
        first = next;
        --size;
        return data;
    }

    /**
     * 删除链表尾部元素
     * @return 该元素
     */
    private T remove(){
        final Node<T> l = last;
        if (l == null) throw new NoSuchElementException();
        T data = l.data;
        final Node<T> previous = l.previous;
        l.previous = null;
        l.data = null;
        if (previous == null){
            current = first = null;
        }else {
            previous.next = null;
        }
        last = previous;
        --size;
        return data;
    }


    public void replace(T data){
        if (current == null) throw new NoSuchElementException();
        current.data = data;
    }
    
    public void clear(){
        for (Node<T> x = first; x != null; ) {
            Node<T> next = x.next;
            x.data = null;
            x.next = null;
            x.previous = null;
            x = next;
        }
        current = first = last = null;
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        Node<T> f = this.first;
        while (f != null){
            sb.append(f.data);
            if (f != last){
                sb.append(",");
            }
            f = f.next;
        }
        sb.append(']');
        return sb.toString();
    }
    
}

package stack;

import java.util.ArrayList;

public class ArrayStack {
    
    ArrayList<Integer> stack = new ArrayList<>(10);

    public void pop() {
        if(stack.isEmpty()) {
            System.out.println("Null");
            return;
        }
        int topIndex = stack.size() - 1;
        System.out.println(stack.get(topIndex));
        stack.remove(topIndex);
    }

    public void push(int n) {
        if(stack.size() == 10) { // optional limit
            System.out.println("Stack overflow! hehe");
            return;
        }
        stack.add(n);
        System.out.println(n);
    }

    public void peek() {
        if(stack.isEmpty()) {
            System.out.println("Null");
            return;
        }
        System.out.println(stack.get(stack.size() - 1));
    }

    public static void main(String[] args) {
        ArrayStack stack = new ArrayStack();
        
        stack.pop();
        stack.push(4);
        stack.push(5);
        stack.push(10);
        stack.push(1);
        stack.push(-1);
        stack.pop();
        stack.pop();
        stack.peek();
    }
}


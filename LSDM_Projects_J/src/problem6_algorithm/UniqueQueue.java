package problem6_algorithm;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class UniqueQueue<E> {
	
	private final Queue<E> queue = new LinkedList<E>();
	private final Set<E> set = new HashSet<E>();

	public void add(E e) {
		if( ! set.contains(e) ) {
			set.add(e);
			queue.add(e);
		}
	}
	
	public E poll() {
		if( ! queue.isEmpty() ) {
			set.remove(queue.peek());
			return queue.poll();
		}else{
			return null;
		}
	}
	
	public boolean contains(E e) {
		return set.contains(e);
	}
	
	public boolean isEmpty(){
		return queue.isEmpty();
	}
}

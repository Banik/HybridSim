package hybridsim.location;

public class Pair<T1, T2> {

	protected T1 a;
	protected T2 b;
	
	public Pair() {
		this.a = null;
		this.b = null;
	}
	
	public Pair(T1 a, T2 b) {
		this.a = a;
		this.b = b;
	}
	
	public Pair(Pair<T1, T2> pair) {
		this.a = pair.getFirst();
		this.b = pair.getSecond();
	}
	
	public T1 getFirst() {
		return this.a;
	}
	
	public T2 getSecond() {
		return this.b;
	}
	
	public Pair<T1, T2> setFirst(T1 a) {
		this.a = a;
		return this;
	}
	
	public Pair<T1, T2> setSecond(T2 b) {
		this.b = b;
		return this;
	}
	
	public boolean isFull() {
		return (this.a != null && this.b != null);
	}
	
}

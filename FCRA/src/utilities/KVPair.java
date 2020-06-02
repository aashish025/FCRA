package utilities;

public class KVPair <K, V>  {
		private K k;
		private V v;
		
		public KVPair (K key, V value)
		{
			this.k = key;
			this.v = value;
		}
		public K getK() {
			return k;
		}
		
		public void setK(K key) {
			this.k = key;
		}
		
		public V getV() {
			return v;
		}
		
		public void setV(V value) {
			this.v = value;
		}
}

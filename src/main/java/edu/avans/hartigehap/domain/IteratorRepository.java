package edu.avans.hartigehap.domain;

import java.util.ArrayList;

public class IteratorRepository extends IteratorContainer{

	public ArrayList<Object> objects;
	
	public IteratorRepository(ArrayList<Object> objects){
		this.objects = objects;
	}
	
	@Override
	public IteratorPattern getIterator(){
		return new ObjectIterator();
	}
	
	private class ObjectIterator extends IteratorPattern{
		int index;
		
		@Override
		public boolean hasNext(){
			if (index < objects.size()){
				return true;
			}
			return false;
		}
		
		@Override
		public Object next(){
			if (this.hasNext()){
				return objects.get(index++);
			}
			return null;
		}
	}
	
}

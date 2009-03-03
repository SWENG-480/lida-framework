package edu.memphis.ccrg.lida.perception;

public class PAMContent implements PAMContentInterface{

	private Percept p;

	public PAMContent(){
		p = new Percept();
	}
	
	public PAMContent(Percept p){
		this.p = p;
	}
	
	public void setNodes(Percept p){
		this.p = p;
	}
	
	public Object getContent(){
		return p;
	}
	
	public void print(){
		p.print();
	}

}

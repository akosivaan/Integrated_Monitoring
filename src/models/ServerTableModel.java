package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import entity.Server;

public class ServerTableModel extends AbstractTableModel {
	
	private Vector<String> columns;
	private Vector<Vector<Object>> data;
	
	public ServerTableModel() {
		// TODO Auto-generated constructor stub
		super();
		columns = new Vector<String>();
		columns.add("ID");
		columns.add("Server Name");
		columns.add("Server Address");
		columns.add("Server Type");
		
		data = new Vector<Vector<Object>>();
	}
	
	public ServerTableModel(ArrayList<Server> svr){
		super();
		columns = new Vector<String>();
		columns.add("ID");
		columns.add("Server Name");
		columns.add("Server Address");
		columns.add("Server Type");
		
		data = new Vector<Vector<Object>>();
		Iterator<Server> itr = svr.iterator();
		while(itr.hasNext()){
			Server sr = itr.next();
			Vector<Object> obj = new Vector<Object>();
			obj.add(sr.getId());
			obj.add(sr.getName());
			obj.add(sr.getAddress());
			obj.add(sr.getType());
			data.add(obj);
		}
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columns.size();
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return data.get(arg0).get(arg1);
	}
	
	@Override
	public String getColumnName(int columnIndex){
		return columns.get(columnIndex);
	}

}

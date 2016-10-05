package models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import entity.FileSystem;
import entity.Server;

public class FilesystemTableModel extends AbstractTableModel {
	private Vector<String> columnName;
	private Vector<Vector<Object>> data;
	
	public FilesystemTableModel() {
		// TODO Auto-generated constructor stub
		super();
		columnName = new Vector<String>();
		columnName.add("ID");
		columnName.add("Server");
		columnName.add("Filesystem");
		
		data = new Vector<Vector<Object>>();
	}
	
	public FilesystemTableModel(ArrayList<FileSystem> fs){
		super();
		columnName = new Vector<String>();
		columnName.add("ID");
		columnName.add("Server");
		columnName.add("Filesystem");
		
		data = new Vector<Vector<Object>>();
		Iterator<FileSystem> itr = fs.iterator();
		while(itr.hasNext()){
			FileSystem sr = itr.next();
			Vector<Object> obj = new Vector<Object>();
			obj.add(sr.getId());
			obj.add(sr.getServerName());
			obj.add(sr.getFileSystem());
			data.add(obj);
		}
	}

	public int getColumnCount() {
		// TODO Auto-generated method stub
		return columnName.size();
	}

	public int getRowCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		return data.get(rowIndex).get(columnIndex);
	}
	

	@Override
	public String getColumnName(int columnIndex){
		return columnName.get(columnIndex);
	}
}

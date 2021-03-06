package entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import utilities.DatabaseUtil;

public class FSRecord {
	private int id;
	private int fs_id;
	private String filesystem;
	private BigDecimal free_space;
	private BigDecimal total_space;
	private BigDecimal perc_used;
	private BigDecimal diff_from_last;
	private Timestamp date_checked;
	public FSRecord() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Description: Constructor for the Filesystem Record that is not yet saved in the database
	 * @param fs_id ID of the filesystem
	 * @param filesystem name of the filesystem
	 * @param free_space number of free space
	 * @param total_space total number of space
	 * @param perc_used percentage used
	 * @param diff_from_last difference of used space from the last record
	 * @param date_checked date check of the record
	 */
	public FSRecord(int fs_id,String filesystem, BigDecimal free_space,BigDecimal total_space, BigDecimal perc_used, BigDecimal diff_from_last, Timestamp date_checked){
		this.setFs_id(fs_id);
		this.setFilesystem(filesystem);
		this.setFree_space(free_space);
		this.setTotal_space(total_space);
		this.setPerc_used(perc_used);
		this.setDiff_from_last(diff_from_last);
		this.setDate_checked(date_checked);

	}
	
	/**
	 * Description: Constructor for the Filesystem Record that was already saved in the database
	 * @param id ID of the record generated by the database
	 * @param fs_id ID of the record generated by the database
	 * @param filesystem name of the filesystem
	 * @param free_space number of free space
	 * @param total_space total number of space
	 * @param perc_used percentage used
	 * @param diff_from_last difference of used space from the last record
	 * @param date_checked date check of the record
	 */
	public FSRecord(int id,int fs_id,String filesystem, BigDecimal free_space,BigDecimal total_space, BigDecimal perc_used, BigDecimal diff_from_last, Timestamp date_checked){
		this.id = id;
		this.setFs_id(fs_id);
		this.setFilesystem(filesystem);
		this.setFree_space(free_space);
		this.setTotal_space(total_space);
		this.setPerc_used(perc_used);
		this.setDiff_from_last(diff_from_last);
		this.setDate_checked(date_checked);
	}

	/**
	 * Description: Getter of the filesystem id
	 * @return filesystem id
	 */
	public int getFs_id() {
		return fs_id;
	}

	/**
	 * Description: Setter of the filesystem id
	 * @param fs_id filesystem id
	 */
	public void setFs_id(int fs_id) {
		this.fs_id = fs_id;
	}

	/**
	 * Description: Getter of the filesystem name
	 * @return name of the filesystem
	 */
	public String getFilesystem() {
		return filesystem;
	}

	/**
	 * Description: Setter of the filesystem name
	 * @param filesystem name of the filesystem
	 */
	public void setFilesystem(String filesystem) {
		this.filesystem = filesystem;
	}

	/**
	 * Description: Getter of the free space of the filesystem
	 * @return free space of the filesystem
	 */
	public BigDecimal getFree_space() {
		return free_space;
	}

	/**
	 * Description: Setter of the free space
	 * @param free_space2 free space of the filesystem
	 */
	public void setFree_space(BigDecimal free_space2) {
		this.free_space = free_space2;
	}

	/**
	 * Description: Getter of the total space of the filesystem
	 * @return total space of the filesystem
	 */
	public BigDecimal getTotal_space() {
		return total_space;
	}

	/**
	 * Description: Setter of the total space
	 * @param total_space2 total space of the filesystem
	 */
	public void setTotal_space(BigDecimal total_space2) {
		this.total_space = total_space2;
	}

	/**
	 * Description: Getter of the percent used of the filesystem
	 * @return percentage of the used space
	 */
	public BigDecimal getPerc_used() {
		return perc_used;
	}

	/**
	 * Description: Setter of the percent used
	 * @param perc_used2 percentage of the used space
	 */
	public void setPerc_used(BigDecimal perc_used2) {
		this.perc_used = perc_used2;
	}

	/**
	 * Description: Getter of the difference from the last record
	 * @return difference of the space used from the last record
	 */
	public BigDecimal getDiff_from_last() {
		return diff_from_last;
	}

	/**
	 * Description: Setter of the difference from last record
	 * @param diff_from_last2 difference of the space used from the last record
	 */
	public void setDiff_from_last(BigDecimal diff_from_last2) {
		this.diff_from_last = diff_from_last2;
	}

	/**
	 * Description: Getter of the date of the filesystem record
	 * @return the date when record was checked
	 */
	public Timestamp getDate_checked() {
		return date_checked;
	}

	/**
	 * Description: Setter of the date checked
	 * @param date_checked the date when record was checked
	 */
	public void setDate_checked(Timestamp date_checked) {
		this.date_checked = date_checked;
	}
	
	/**
	 * Description: Save the record
	 */
	public void saveRecord(){
		DatabaseUtil.addFSData(this);
	}
	

}

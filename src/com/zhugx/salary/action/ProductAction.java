package com.zhugx.salary.action;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.zhugx.salary.pojo.ProductData;
import com.zhugx.salary.pojo.ProductPrice;
import com.zhugx.salary.pojo.ProductTime;
import com.zhugx.salary.pojo.common.DataGrid;
import com.zhugx.salary.pojo.common.DataGridProductData;
import com.zhugx.salary.service.MonthSalaryService;
import com.zhugx.salary.service.ProductService;
import com.zhugx.salary.webservice.CheGong;
import com.zhugx.salary.webservice.Others;

@SuppressWarnings("all")
public class ProductAction extends ActionSupport {
	private DataGrid dataGrid;
	private DataGridProductData productDataGrid;
	private String dataArray;
	private String dataCgArray;
	private String dataQtArray;
	private String priceArray;
	private String timeArray;
	private String salaryArray;
	private String message;
	private List<String> groupName;
	private ProductService productService;
	private MonthSalaryService salaryService;

	
	public void setGroupName(List<String> groupName) {
		this.groupName = groupName;
	}
	public List<String> getGroupName() {
		return groupName;
	}
	@Resource
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DataGridProductData getProductDataGrid() {
		return productDataGrid;
	}
	public void setProductDataGrid(DataGridProductData productDataGrid) {
		this.productDataGrid = productDataGrid;
	}
	public ProductService getProductService() {
		return productService;
	}
	public DataGrid getDataGrid() {
		return dataGrid;
	}
	public void setDataGrid(DataGrid dataGrid) {
		this.dataGrid = dataGrid;
	}
	public String getDataArray() {
		return dataArray;
	}
	public String getDataCgArray() {
		return dataCgArray;
	}
	public void setDataCgArray(String dataCgArray) {
		this.dataCgArray = dataCgArray;
	}
	public String getDataQtArray() {
		return dataQtArray;
	}
	public void setDataQtArray(String dataQtArray) {
		this.dataQtArray = dataQtArray;
	}
	public void setDataArray(String dataArray) {
		this.dataArray = dataArray;
	}
	public String getPriceArray() {
		return priceArray;
	}
	public void setPriceArray(String priceArray) {
		this.priceArray = priceArray;
	}
	public String getTimeArray() {
		return timeArray;
	}
	public void setTimeArray(String timeArray) {
		this.timeArray = timeArray;
	}
	public String getSalaryArray() {
		return salaryArray;
	}
	public void setSalaryArray(String salaryArray) {
		this.salaryArray = salaryArray;
	}
	public MonthSalaryService getSalaryService() {
		return salaryService;
	}
	@Resource
	public void setSalaryService(MonthSalaryService salaryService) {
		this.salaryService = salaryService;
	}
	//===================================================================
	// logic method
	//====================================================================
	//3. 工时
	/** 重新获取工时分  */
	public String reLoadTime(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String orgId = request.getParameter("orgId");
		this.productService.reLoadTime(month, orgId);
		
		message = SUCCESS;
		return SUCCESS;
	}
	
	/** 删除工时 */
	public String delTime(){
		JSONArray ja = JSONArray.fromObject(dataArray);
    	List list = JSONArray.toList(ja, ProductTime.class);
    	this.productService.delTime(list);
    	message = SUCCESS;
    	return SUCCESS;
	}
	
	/** 获取 工时 */
	public String getProductTime(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String orgId = request.getParameter("orgId");
		dataGrid = this.productService.getProductTime(month,orgId);
		return SUCCESS;
	}
	
	/** 保存 工时*/
	public String saveOrUpdateProductTime(){
		JSONArray ja = JSONArray.fromObject(dataArray);
    	List list = JSONArray.toList(ja, ProductTime.class);
    	this.productService.saveOrUpdateProductTime(list);
    	message = SUCCESS;
		return SUCCESS;
	}
	public String saveOrUpdateProductTimeCallback(){
		JSONArray ja = JSONArray.fromObject(dataArray);
    	List list = JSONArray.toList(ja, ProductTime.class);
    	this.productService.saveOrUpdateProductTimeCallback(list);
    	message = SUCCESS;
		return SUCCESS;
	}
		
	//2. 工价
	/** 获取 工价 */
	public String getProductPrice(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String shop = request.getParameter("shop");
		
		dataGrid = this.productService.getProductPrice(shop,month);
		return SUCCESS;
	}
	
	/** 保存 工价*/
	public String saveOrUpdateProductPrice(){
		JSONArray ja = JSONArray.fromObject(dataArray);
    	List list = JSONArray.toList(ja, ProductPrice.class);
    	this.productService.saveOrUpdateProductPrice(list);
    	message = SUCCESS;
		return SUCCESS;
	}
	public String saveOrUpdateProductPriceCallback(){
		JSONArray ja = JSONArray.fromObject(dataArray);
    	List list = JSONArray.toList(ja, ProductPrice.class);
    	this.productService.saveOrUpdateProductPriceCallback(list);
    	message = SUCCESS;
		return SUCCESS;
	}
	
	/** 获取上个月工价 */
	public String  userLastMonthPrice(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String shop = request.getParameter("shop");
		
		this.productService.userLastMonthPrice(shop,month);
		message = SUCCESS;
		return SUCCESS;
	}
	
	//1. 生产数据
	/**  初始化/重新获取 生产数据 */
	public String reloadProductData(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String shop = request.getParameter("shop");
		JSONArray jaQt = JSONArray.fromObject(dataQtArray);
		List<Others> listQt = JSONArray.toList(jaQt, Others.class);
		JSONArray jaCg = JSONArray.fromObject(dataCgArray);
    	List<CheGong> listCg = JSONArray.toList(jaCg, CheGong.class);
		
		//如果没有本月数据, 那么就初始化本月数据
    	/*DataGridProductData temp = this.productService.getProductDataByMonth(shop,month,false);
		if(temp == null || temp.getTotal() == 0){
			//根据车间shop 获取 车工组 UUID
			this.productService.initProductData(shop,month,listCg, listQt);
		}else{
			this.productService.reloadProductData(shop,month,listCg, listQt);
		}*/
    	//根据车间shop 获取 车工组 UUID
		this.productService.initProductData(shop,month,listCg, listQt);
		this.message = SUCCESS;
		return SUCCESS;
	}
	
	/**  初始化/重新获取 生产数据   回调函数   由于初始化时间太长, 剩下的由回调函数初始化*/
	public String reloadProductDataCallback(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String shop = request.getParameter("shop");
		JSONArray jaQt = JSONArray.fromObject(dataQtArray);
		List<Others> listQt = JSONArray.toList(jaQt, Others.class);
		JSONArray jaCg = JSONArray.fromObject(dataCgArray);
    	List<CheGong> listCg = JSONArray.toList(jaCg, CheGong.class);
		
		//如果没有本月数据, 那么就初始化本月数据
		DataGridProductData temp = this.productService.getProductDataByMonth(shop,month,false);
		if(temp == null || temp.getTotal() == 0){
			//根据车间shop 获取 车工组 UUID
			this.productService.initProductDataCallback(shop,month,listCg, listQt);
		}
		this.message = SUCCESS;
		return SUCCESS;
	}
	
	
	/** 获取 生产数据 (车工)*/
	public String getProductData(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String shop = request.getParameter("shop");
		productDataGrid = this.productService.getProductDataByMonth(shop,month,false);
		return SUCCESS;
	}
	/** 获取 生产数据 (其他工段 修改数量*/
	public String getProductQtData(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String shop = request.getParameter("shop");
		dataGrid = this.productService.getProductQtData(shop, month);
		return SUCCESS;
	}
	
	/** 获取 车工计件工资明细表 */
	public String getProductDataCg(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String shop = request.getParameter("shop");
		
		productDataGrid = this.productService.getProductDataByMonth(shop,month,true);
		return SUCCESS;
	}
	
	/** 保存 生产数据*/
	public String saveOrUpdateProductData(){
		JSONArray ja = JSONArray.fromObject(dataArray);
    	List list = JSONArray.toList(ja, ProductData.class);
    	this.productService.saveOrUpdateProductData(list);
    	message = SUCCESS;
		return SUCCESS;
	}
	public String saveOrUpdateProductDataCallback(){
		System.out.println("saveOrUpdateProductDataCallback");
    	HttpServletRequest  request = ServletActionContext.getRequest();
		String month = request.getParameter("month");
		String shop = request.getParameter("shop");
		this.productService.saveOrUpdateProductDataCallback(shop, month);
		
    	message = SUCCESS;
		return SUCCESS;
	}
	
	/** 保存 生产数据   裁剪  手工  烫工数量*/
	public String saveOrUpdateProductDataQt(){
		JSONArray ja = JSONArray.fromObject(dataArray);
    	List list = JSONArray.toList(ja, ProductData.class);
    	this.productService.saveOrUpdateProductDataQt(list);
    	message = SUCCESS;
		return SUCCESS;
	}
	public String saveOrUpdateProductDataQtCallback(){
		JSONArray ja = JSONArray.fromObject(dataArray);
    	List list = JSONArray.toList(ja, ProductData.class);
    	this.productService.saveOrUpdateProductDataQtCallback(list);
    	message = SUCCESS;
		return SUCCESS;
	}
	
	/** 查询所有的 小组名 */
	public String queryGroupName(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String shop = request.getParameter("shop");
		String month = request.getParameter("date");
		groupName = this.productService.getGroupName(shop,month);
		message = SUCCESS;
		return SUCCESS;
	}
	
	/** 查询所有的 小组 编号 */
	public String queryGroupNo(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String shop = request.getParameter("shop");
		groupName = this.productService.getGroupNo(shop);
		message = SUCCESS;
		return SUCCESS;
	}
	
	/** 清空本月数据 */
	public String cleanData(){
		HttpServletRequest  request = ServletActionContext.getRequest();
		String shop  = request.getParameter("shop");
		String month = request.getParameter("month");
		this.productService.cleanData(shop, month);
		
		message = SUCCESS;
		return SUCCESS;
	}
	
}

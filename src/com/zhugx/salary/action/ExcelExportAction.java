package com.zhugx.salary.action;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.zhugx.salary.pojo.Attendance;
import com.zhugx.salary.pojo.MonthAnalyze;
import com.zhugx.salary.pojo.MonthSalary;
import com.zhugx.salary.pojo.OrgSalary;
import com.zhugx.salary.pojo.ProductData;
import com.zhugx.salary.pojo.ProductPrice;
import com.zhugx.salary.pojo.ProductTime;
import com.zhugx.salary.pojo.common.ColModel;
import com.zhugx.salary.tool.ZhugxUtils;

public class ExcelExportAction extends ActionSupport {
    private InputStream excelStream;  //输出流变量
    private String excelFileName; //下载文件名
    private String message;

	public InputStream getExcelStream() {
        return excelStream;
    }
    public void setExcelStream(InputStream excelStream) {
        this.excelStream = excelStream;
    }
    public String getExcelFileName() {
        return excelFileName;
    }
    public void setExcelFileName(String excelFileName) {
        this.excelFileName = excelFileName;
    }
    public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	//===================================================================
	public Map<String, Class> getService() {
    	Map<String, Class> service = new HashMap<String, Class>();
    	service.put("ProductData", ProductData.class);
    	service.put("ProductPrice", ProductPrice.class);
    	service.put("ProductTime", ProductTime.class);
    	service.put("OrgSalary", OrgSalary.class);
    	service.put("MonthSalary", MonthSalary.class);  
    	service.put("MonthAnalyze", MonthAnalyze.class); 
    	service.put("Attendance", Attendance.class);
		return service;
	}
    
	
    /** 保存数据 */
    @SuppressWarnings("all")
	public String saveListData(){
    	HttpServletRequest  request = ServletActionContext.getRequest();
    	request.getSession().setAttribute("listData", request.getParameter("dataArray"));
    	request.getSession().setAttribute("colArray", request.getParameter("colArray"));
    	message = SUCCESS;
    	return SUCCESS;
    }
	
	/** 导出Excel测试 */
	 @SuppressWarnings("all")
	public String exportExcel() throws Exception {
		HttpServletRequest  request = ServletActionContext.getRequest();
		 
		//1.列模型
		JSONArray jaCol = JSONArray.fromObject(request.getSession().getAttribute("colArray"));
		List<ColModel> listCol = JSONArray.toList(jaCol, ColModel.class);
		//2.获取标题
		String title = request.getParameter("title");
		//3. 导出的数据
		String pojo = request.getParameter("pojo");
		//4.导出的数据
    	JSONArray jaData = JSONArray.fromObject(request.getSession().getAttribute("listData"));
		List listData = JSONArray.toList(jaData, this.getService().get(pojo));
		
		//第一步，创建一个webbook，对应一个Excel文件
        HSSFWorkbook wb = new HSSFWorkbook();
        //第二步，在webbook中添加一个sheet，对应Excel文件中的 sheet
        HSSFSheet sheet = wb.createSheet(title);
        //第三步，在sheet中添加标题第0行， 表头第一行, 
        HSSFCell cell;
        HSSFCellStyle styleTitle = wb.createCellStyle();
        styleTitle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        HSSFFont font = wb.createFont();  
        font.setFontHeightInPoints((short)20);  
        font.setFontName("新宋体");  
        font.setBoldweight((short) 1);  
        styleTitle.setFont(font);
        //标题
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, listCol.size()-1)); 
        HSSFRow row = sheet.createRow(0);
        row.setHeight((short)600);
        cell = row.createCell(0);
        cell.setCellValue(title); 
        cell.setCellStyle(styleTitle);
        
        row = sheet.createRow(1); 
         //表头
        HSSFFont font2 = wb.createFont();  
        font2.setFontHeightInPoints((short)12);  
        font2.setFontName("新宋体");  
        font2.setBoldweight((short) 1);
        
        HSSFCellStyle styleTitle2 = wb.createCellStyle();
        styleTitle2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        styleTitle2.setFont(font2);
        
        for(int i=0; i<listCol.size(); i++){
        	 cell = row.createCell(i);
        	 cell.setCellValue(listCol.get(i).getTitle().replace("&nbsp;", ""));
        	 cell.setCellStyle(styleTitle2);
        }
        //第六步，写入实体数据
		for(int k=2; k<=listData.size()+1; k++){
			row = sheet.createRow(k);
			for(int i=0; i<listCol.size(); i++){
				String method = "get"+ZhugxUtils.captureName(listCol.get(i).getField());
				Object obj = listData.get(k-2).getClass().getMethod(method, new Class[]{}).invoke(listData.get(k-2), new Object[]{});
				if(obj != null){
            		row.createCell(i).setCellValue(obj.toString());
            	}else{
            		row.createCell(i).setCellValue("");
            	}
			}
		}
		
		//第七步，将文件存到流中
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        wb.write(os);
        byte[] fileContent = os.toByteArray();
        ByteArrayInputStream is = new ByteArrayInputStream(fileContent);

        excelStream = is;             //文件流 
        excelFileName = new String(title.getBytes(), "ISO8859-1")+".xls"; //设置下载的文件名  
		
		return "success";
	}
}

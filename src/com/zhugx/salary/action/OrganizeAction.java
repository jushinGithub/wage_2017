package com.zhugx.salary.action;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;
import com.zhugx.salary.pojo.Organize;
import com.zhugx.salary.pojo.TreeNode;
import com.zhugx.salary.service.OrganizeService;

@SuppressWarnings("all")
@Controller
public class OrganizeAction extends ActionSupport {
	private List<TreeNode> treeNodes;
	@Resource
	private OrganizeService  organizeService;
	
	public void setOrganizeService(OrganizeService organizeService) {
		this.organizeService = organizeService;
	}
	public List<TreeNode> getTreeNodes() {
		return treeNodes;
	}
	public void setTreeNodes(List<TreeNode> treeNodes) {
		this.treeNodes = treeNodes;
	}
	//==============================================================
	//  logic method 
	//==============================================================
	
	/**
	 * 增加计时部门
	 * @return
	 * @throws Exception
	 */
	public String addTimeOrg() throws Exception{
		HttpServletRequest  request = ServletActionContext.getRequest();
		String orgName = request.getParameter("orgName");
		String orgId = request.getParameter("orgId");
		this.organizeService.addTimeOrg(orgName,orgId);
		return SUCCESS;
	}
	
	/**
	 * 获取组织部门
	 * @return
	 * @throws Exception
	 */
	public String getOrgTreeNodes() throws Exception {
		HttpServletRequest  request = ServletActionContext.getRequest();
		String pid = request.getParameter("pid");
		if(pid == null){
			pid = "-1";
		}
		List<Organize> root = this.organizeService.getOrgTreeById(pid);
		List<TreeNode> rootNode = this.transferDataToTreeNode(root);
		
		treeNodes = formateTreeNode(rootNode);
		return SUCCESS;
	}
	
	/**
	 * 获取车间
	 * @return
	 */
	public String getShop(){
		treeNodes = this.transferDataToTreeNode(this.organizeService.getShop());
		return SUCCESS;
	}
	
	/** 转换成 树形格式 满足easyui tree*/
	public List<TreeNode> formateTreeNode(List<TreeNode> rootNode){
		for(int i=0; i<rootNode.size(); i++){
			List<Organize> children = this.organizeService.getOrgTreeById(rootNode.get(i).getId());
			List<TreeNode> childrenNode = transferDataToTreeNode(children);
			
			formateTreeNode(childrenNode);
			rootNode.get(i).setChildren(childrenNode);
		}
		return rootNode;
	}
	
	/** 转换成 树形格式 满足easyui tree   子节点 */
	public List<TreeNode>  transferDataToTreeNode(List<Organize> root){ 
		List<TreeNode>  nodes = new ArrayList<TreeNode>();
		for(Organize temp: root){
			TreeNode nodeTemp = new TreeNode();
			nodeTemp.setId(temp.getUuid());
			nodeTemp.setText(temp.getOrgName());
			nodeTemp.setPid(temp.getPid());
			nodes.add(nodeTemp);
		}
		return nodes;
	}

}

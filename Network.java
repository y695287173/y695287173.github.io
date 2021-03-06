package com.huawei.exam;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Network 
{
    Map<String,ArrayList<Integer>> nodeValue = new HashMap<String,ArrayList<Integer>>();
    Map<String,ArrayList<String>> mapPath = new HashMap<String,ArrayList<String>>();
    ArrayList<String> result ;
    Stack<String> nodeList;
    
    public boolean readNetworkRelation(String inputFile)
    {
    	String sTem;
    	Integer value;
    	Integer delay;
    	
    	if (inputFile==null ||inputFile.length()==0)
    		return false;
    	
    	File file = new File(inputFile);
    	BufferedReader bufread;  
    	String read; 
        try {
			bufread = new BufferedReader(new FileReader(file));
			while ((read = bufread.readLine()) != null) {  
				//过滤空行
	            sTem = read.trim();
	            if(sTem.length()==0) continue;
	            
	    		String[] pathItem = sTem.split(",");
	    		if (pathItem == null || pathItem.length!=3){
	    			return false;
	    		}
	    		
	    		for(int j =0;j<3;j++){
	    			pathItem[j] = pathItem[j].trim();
	    		}
	    		
	    		//过滤时延中包含非正整数
	    		if(pathItem[2].contains(".") || pathItem[2].startsWith("-") )
	    			return false;

	    		//添加节点价值和时延信息到nodeValue
	    		for(int j =0;j<2;j++){
	    			//字符串过滤
	    		    String regEx = "[a-zA-Z0-9]{1,20}";
	    		    Pattern pattern = Pattern.compile(regEx);
	    		    Matcher matcher = pattern.matcher(pathItem[j]);
	    		    boolean res = matcher.matches();
	    			if(!res) return false;
	    			
	    			//添加节点价值和时延信息到nodeValue
	    			ArrayList<Integer> list = nodeValue.get(pathItem[j]);
	        		if(list == null || list.size() == 0){
	        			list = new ArrayList<Integer>();
	        			list.add(1);
	        			list.add(Integer.parseInt(pathItem[2]));
	        			nodeValue.put(pathItem[j], list);
	        		}else{
	        			value = list.get(0);
	        			delay = list.get(1);
	        			list.set(0, ++value);
	        			list.set(1, delay+Integer.valueOf(pathItem[2]));
	        		}
	    		}
	    		
	    		//添加路径到mapPath中
	    		ArrayList<String> dstArr = mapPath.get(pathItem[0]);
	    		
	    		if(dstArr == null){
	    			dstArr = new ArrayList<String>();
	    			dstArr.add(pathItem[1]);
	    			mapPath.put(pathItem[0], dstArr);
	    		}else{
	    			dstArr.add(pathItem[1]);
	    		}
	        }  
			bufread.close(); 
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}  catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}  
		
        return true;
    }
    
    //节点排序，先比较节点价值，相同比较节点总时延，仍相同按字符串顺序输出
    private class valueComparator implements Comparator<Map.Entry<String,ArrayList<Integer>>>    
    {    
        public int compare(Map.Entry<String,ArrayList<Integer>> item1, Map.Entry<String,ArrayList<Integer>> item2)     
        {    
        	int value1 = item1.getValue().get(0);
        	int value2 = item2.getValue().get(0);
        	if(value1 > value2)
        		return -1;
        	else if(value1 < value2)
        		return 1;
        	else{
        		int delay1 = item1.getValue().get(1);
            	int delay2 = item2.getValue().get(1);
            	if(delay1 < delay2)
            		return -1;
            	else if(delay1 > delay2)
            		return 1;
            	else{
            		return item1.getKey().compareTo(item2.getKey());
            	}
        	}
        }    
    }  
    //使用valueComparator排序nodeValue，然后进行输出
    public String sortNetworkNodes()
    {
        List<Map.Entry<String,ArrayList<Integer>>> list=new ArrayList<Map.Entry<String,ArrayList<Integer>>>();  
        list.addAll(nodeValue.entrySet());  
        Collections.sort(list,new valueComparator());  
        StringBuilder sb = new StringBuilder();
        
        for(Map.Entry<String,ArrayList<Integer>> item : list){
        	sb.append(item.getKey()+",");
        }
        
        if(sb.length()!=0)	sb.deleteCharAt(sb.length()-1);
//        System.out.println(sb.toString());
        return sb.toString();
    }
    
    //递归深度优先遍历
    private void fintPath(String start, String end)  
    {
    	nodeList.push(start);
    	ArrayList<String> children = mapPath.get(start);
    	
    	if(children == null){
    		nodeList.pop();  
    		return;
    	}
    	
    	//如果找寻到目标节点，将栈中存放的数据按压入顺序取出，输出路径到result
    	for(int i=0;i<children.size();i++){
    		if(end.equals(children.get(i))){
    			StringBuilder sb = new StringBuilder();
    			for(int j=0;j<nodeList.size();j++){
    				sb.append(nodeList.get(j)+",");
    			}
    			sb.append(end);
    			result.add(sb.toString());
    			nodeList.pop(); 
    			return;
    		}
    	}
    	
    	//递归查找子节点,避免形成环
    	for(int i = 0; i < children.size(); i++)  
        {  
    		if(!nodeList.contains(children.get(i)))
    			fintPath(children.get(i), end);  
        } 
    	
    	//遍历完成节点被pop出nodeList
        nodeList.pop();  
    }
    
    public String getShortestPath(String start,String end)
    {
    	//搜索路径时，用于存储经过节点
    	nodeList = new Stack<String>();
    	result = new ArrayList<String>();
    	
    	if(start.equals(end)) return "NoPath";
    	
    	fintPath(start,end);

    	Collections.sort(result,new Comparator<String>() {

			@Override
			public int compare(String s1, String s2) {
				// TODO Auto-generated method stub
				return s1.length()-s2.length();
			}
		}); 
    	
    	//输出result中最短路径到字符串
    	StringBuilder sb = new StringBuilder();
    	for(String s : result){
    		if(s.length() == result.get(0).length())
    			sb.append(s+";");
    		else
    			break;
    	}
    	//去掉末尾分号
    	if(sb.length()!=0)
    		sb.deleteCharAt(sb.length()-1);
    	else
    		return "NoPath";
    	
		return sb.toString();
    }
    
}

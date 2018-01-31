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
    int[] visit;
    ArrayList<String> keyToNum;
    
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
    
    public boolean readNetworkRelation(String inputFile)
    {
    	String tem = inputFile.trim();
    	String[] strTem = tem.split("\n");
    	String sTem;
    	Integer value;
    	Integer delay;
    	
    	if (inputFile==null ||inputFile.length()==0)
    		return false;
    	
    	File file = new File(inputFile);
        // 读取文件，并且以utf-8的形式写出去  
    	BufferedReader bufread;  
    	String read; 
        try {
			bufread = new BufferedReader(new FileReader(file));
			while ((read = bufread.readLine()) != null) {  
	            System.out.println(read);  
	            sTem = read.trim();
	            if(sTem.length()==0) continue;
	    		String[] pathItem = sTem.split(",");
	    		if (pathItem == null || pathItem.length!=3){
	    			return false;
	    		}
	    		
	    		for(int j =0;j<3;j++){
	    			pathItem[j] = pathItem[j].trim();
	    		}
	    		
	    		if(pathItem[2].contains(".") || pathItem[2].startsWith("-") )
	    			return false;

	    		for(int j =0;j<2;j++){
	    		    String regEx = "[a-zA-Z0-9]{1,20}";
	    		    Pattern pattern = Pattern.compile(regEx);
	    		    // 忽略大小写的写法
	    		    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	    		    Matcher matcher = pattern.matcher(pathItem[j]);
	    		    // 字符串是否与正则表达式相匹配
	    		    boolean res = matcher.matches();
	    			if(!res) return false;
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
	    		
	    		ArrayList<String> dstArr = mapPath.get(pathItem[0]);
	    		
	    		if(dstArr == null){
	    			dstArr = new ArrayList<String>();
	    			dstArr.add(pathItem[1]);
	    			mapPath.put(pathItem[0], dstArr);
	    		}else{
	    			System.out.println("log:  "+pathItem[0]+"    "+dstArr.toString());
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
		
        Set<String> keys = nodeValue.keySet();
        ArrayList<Integer> list;
    	for(String key : keys){
    		list = nodeValue.get(key);
    		System.out.println(key+" : value: "+list.get(0)+"  delay: "+list.get(1));
    	}
    	
    	keys = mapPath.keySet();
    	ArrayList<String> list2;
    	for(String key : keys){
    		list2 = mapPath.get(key);
    		System.out.print(key+" : node: ");
    		for(String s : list2){
    			System.out.print(s+", ");
    		}
    		System.out.print(" \n");
    	}
        return true;
    }
    
    public String sortNetworkNodes()
    {
        List<Map.Entry<String,ArrayList<Integer>>> list=new ArrayList<Map.Entry<String,ArrayList<Integer>>>();  
        
        list.addAll(nodeValue.entrySet());  
        Collections.sort(list,new valueComparator());  
        String key;
        StringBuilder sb = new StringBuilder();
        
        for(Map.Entry<String,ArrayList<Integer>> item : list){
        	key = item.getKey();
        	sb.append(key+",");
        }
        sb.deleteCharAt(sb.length()-1);
        System.out.println(sb.toString());
        return sb.toString();
    }
    
    private void fintPath(String start, String end)  
    {
    	visit[keyToNum.indexOf(start)] = 1;
    	nodeList.push(start);
    	ArrayList<String> children = mapPath.get(start);
    	
    	if(children == null){
    		nodeList.pop();  
    		return;
    	}
    	
    	//查看是否查询到目标节点
    	for(int i=0;i<children.size();i++){
    		if(end.equals(children.get(i))){
    			StringBuilder sb = new StringBuilder();
    			for(int j=0;j<nodeList.size();j++){
    				sb.append(nodeList.get(j)+",");
    			}
    			sb.append(end);

    			result.add(sb.toString());
    		}
    	}
    	
    	for(int i = 0; i < children.size(); i++)  
        {  
    		if(!nodeList.contains(children.get(i)))
    			fintPath(children.get(i), end);  
        } 
    	
    	//放在最后可以对满检测过的或者没有的子阶的元素删除  
        int count = 0;  
        for(int i = 0; i < children.size(); i++)  
        {  
            if(visit[keyToNum.indexOf(children.get(i))]==1)  
                count++;  
        }  
        if(count == children.size())  
        	nodeList.pop();  
    }
    
    public String getShortestPath(String start,String end)
    {
    	nodeList = new Stack<String>();
    	result = new ArrayList<String>();
    	Set<String> keys = nodeValue.keySet();
    	int n = keys.size();
    	visit =new int[n];
    	
    	if(start.equals(end)) return "start";
    	
    	//初始化keyToNum
    	keyToNum = new ArrayList<String>();
    	
    	keyToNum.add(start);
    	for(String key : keys){
    		if(key.equals(start)!=true)
    			keyToNum.add(key);
    	}
    	
    	fintPath(start,end);
    	
    	
    	
    	Collections.sort(result,new Comparator<String>() {

			@Override
			public int compare(String s1, String s2) {
				// TODO Auto-generated method stub
				return s1.length()-s2.length();
			}
		}); 
    	
    	StringBuilder sb = new StringBuilder();
    	for(String s : result){
    		if(s.length() == result.get(0).length())
    			sb.append(s+";");
    		else
    			break;
    	}
    	
    	for(String s : result){
    		System.out.println(s);
    	}
    	
    	if(sb.length()!=0)
    		sb.deleteCharAt(sb.length()-1);
    	else
    		return "NoPath";
    	
		return sb.toString();
    }
    
}

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
    
    
    public String getShortestPath(String start,String end)
    {
    	if(start == null || start.length()==0 || end == null || end.length()==0 || start.compareTo(end) == 0 )
    		return "NoPath";
    	
    	//初始化keyToNum
    	Set<String> keys = nodeValue.keySet();
    	int n = keys.size();
    	ArrayList<String> keyToNum = new ArrayList<String>();
    	int path[][] = new int[n][n];
    	int pre[][] = new int[n][n];
    	ArrayList<String> list;
    	
    	keyToNum.add(start);
    	for(String key : keys){
    		if(key.equals(start)!=true)
    			keyToNum.add(key);
    	}
    	
    	//初始化path
    	for(int i=0;i<n;i++){
    		for(int j=0;j<n;j++){
    			path[i][j]=Integer.MAX_VALUE;
    		}
    	}
    	
    	for(String key : keys){
    		list = mapPath.get(key);
    		if(list == null) continue;
    		for(String s : list){
    			path[keyToNum.indexOf(key)][keyToNum.indexOf(s)] = 1;
    		}
    	}
    	
    	//初始化pre和minLen
    	for(int i=0;i<n;i++){
    		for(int j=0;j<n;j++){
    			pre[i][j]=-1;
    		}
    	}
    	
    	int minLen[]=new int[n];
    	int visit[]=new int[n];
    	
    	for(int i=0;i<n;i++){
    		minLen[i]=path[0][i];
    		if(path[0][i] == 1){
    			pre[i][0]=0;
    		}
    			
    	}
    	
    	visit[0] = 1;
    	minLen[0]=0;
    	
    	for(int i=1;i<n;i++){
    		int min=Integer.MAX_VALUE;
    		int minj=0;
    		for(int j=0;j<n;j++){
    			if(visit[j]==0 && minLen[j]<min){
    				min=minLen[j];
    				minj=j;
    			}
    		}
    		visit[minj]=1;
    		for(int j=1;j<n;j++){
    			if(visit[j]==0 && (min+path[minj][j])< minLen[j] && minLen[minj]!=Integer.MAX_VALUE&&path[minj][j]!=
    				Integer.MAX_VALUE){
    				minLen[j] = min+path[minj][j];
    				pre[j][0] = minj;
    				System.out.println("pre: "+j+","+"0"+": "+pre[j][0]);
    			}else if(visit[j]==0 && (min+path[minj][j])== minLen[j] && minLen[minj]!=Integer.MAX_VALUE&&path[minj][j]!=
    				Integer.MAX_VALUE){
    				int l =0;
    				while(pre[j][l]!=-1) l++;
    				pre[j][l] = minj;
    				System.out.println("pre: "+j+","+l+": "+pre[j][l]);
    			}
    		}	
    	}
    	
    	//转化成路径
    	ArrayList<ArrayList<Integer>> arrRes = new ArrayList<ArrayList<Integer>>();
    	ArrayList<ArrayList<Integer>> arrTotal;
    	
    	int p = keyToNum.indexOf(end);
    	int l = 0;
    	if (pre[p][0] == -1)
    		return "NoPath";
    	else{
    		while(pre[p][l] != -1)
    		{
    			ArrayList<Integer> arrStr = new ArrayList<Integer>();
    			arrStr.add(p);
    			arrStr.add(pre[p][l]);
    			arrRes.add(arrStr);
    			l++;
    		}
    	}
    	
    	boolean isend =false;

    	while(!isend){
    		arrTotal = (ArrayList<ArrayList<Integer>>) arrRes.clone();
    		isend =true;
    		for (ArrayList<Integer> arr : arrTotal){
    			int tem = arr.get(arr.size()-1);
    			l = 0;
    			while(pre[tem][l]!=-1){
    				if (isend ==true){
    					isend =false;
    				}
    				if(l==0){
    					arr.add(pre[tem][l]);
    				}else{
    					ArrayList<Integer> arrTem = new ArrayList<Integer>();
    					for(int i=0;i<arr.size()-1;i++){
    						arrTem.add(arr.get(i));
    					}
    					arrTem.add(pre[tem][l]);
    					arrRes.add(arrTem);
    				}
    				l++;
    			}
    		}
    	}

    	//输入结果到result
    	StringBuilder result = new StringBuilder();
    	for(ArrayList<Integer> arr : arrRes){
    		for(int i=arr.size()-1;i>=0;i--){
    			result.append(keyToNum.get(arr.get(i))+","); 
    		}
    		result.deleteCharAt(result.length()-1);
    		result.append(";");
    	}
    	if(result.length()==0)
    		return "NoPath";
    	else
    		result.deleteCharAt(result.length()-1);
    	
    	System.out.println(result.toString());
        return result.toString();
    }
    
}

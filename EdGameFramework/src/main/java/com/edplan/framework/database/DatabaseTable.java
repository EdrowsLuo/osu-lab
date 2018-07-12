package com.edplan.framework.database;
import android.util.Log;
import com.edplan.framework.database.annotation.DBIgnore;
import com.edplan.framework.database.annotation.PrimaryKeyAutoIncrement;
import com.edplan.framework.database.annotation.SQLAddition;
import com.edplan.framework.database.annotation.TableName;
import java.lang.reflect.Field;
import java.util.ArrayList;
import android.database.sqlite.SQLiteDatabase;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Collections;

public class DatabaseTable
{
	private String tableName;
	
	private LineNode[] lineNodes;
	
	private String createTableIfNotExistSQL;
	
	private String updateSQL;
	
	private String insertSQL;
	
	private String selectLastInsertRowid;
	
	private SQLiteDatabase database;
	
	public DatabaseTable(SQLiteDatabase db){
		this.database=db;
	}
	
	public void initial(Class<? extends DatabaseLine> klass){
		tableName=klass.getAnnotation(TableName.class).value();
		StringBuilder createTable=new StringBuilder(String.format("CREATE TABLE IF NOT EXISTS %s (",tableName));
		StringBuilder insert=new StringBuilder(String.format("INSERT INTO %S(",tableName));
		ArrayList<LineNode> nodes=new ArrayList<LineNode>();
		int checkedFields=0;
		boolean hasPrimaryKey=false;
		boolean hasAppendValue=false;
		for(Field field:klass.getFields()){
			if(field.isAnnotationPresent(DBIgnore.class)){
				continue;
			}
			if(!field.isAnnotationPresent(DatabaseIndex.class))continue;
			final int index=field.getAnnotation(DatabaseIndex.class).value();
			final Class dclass=field.getType();
			final DataType dataType;
			if(dclass.equals(int.class)||dclass.equals(Integer.class)){
				dataType=DataType.Int;
			}else if(dclass.equals(long.class)){
				dataType=DataType.Long;
			}else if(dclass.equals(String.class)){
				dataType=DataType.Text;
			}else if(dclass.equals(float.class)||dclass.equals(double.class)){
				dataType=DataType.Real;
			}else{
				Log.w("DatabaseLine","field @"+field.getName()+" isn't support datatype, you should add a DBIgnore annotation");
				continue;
			}
			final LineNode n=new LineNode();
			n.index=index;
			n.isPrimaryKeyAutoIncrement=field.isAnnotationPresent(PrimaryKeyAutoIncrement.class);
			hasPrimaryKey|=n.isPrimaryKeyAutoIncrement;
			n.field=field;
			n.type=dataType;
			n.name=field.getName();
			nodes.add(n);
			
		}
		
		Collections.sort(nodes,new Comparator<LineNode>(){
				@Override
				public int compare(DatabaseTable.LineNode p1,DatabaseTable.LineNode p2){

					return p1.index-p2.index;
				}
			});
			
		for(final LineNode n:nodes){
			checkedFields++;
			if(checkedFields!=1){
				createTable.append(',');
				if(hasAppendValue)insert.append(',');
			}
			if(!n.isPrimaryKeyAutoIncrement){
				insert.append(n.name);
				hasAppendValue=true;
			}
			createTable.append(String.format("%s %s",n.name,n.type.SQLType));
			if(n.field.isAnnotationPresent(PrimaryKeyAutoIncrement.class)){
				createTable.append(" PRIMARY KEY AUTOINCREMENT");
			}
			if(n.field.isAnnotationPresent(SQLAddition.class)){
				createTable.append(String.format(" %s",n.field.getAnnotation(SQLAddition.class).value()));
			}
		}
		
		selectLastInsertRowid=String.format("select last_insert_rowid() from %s",tableName);
		
		createTable.append(')');
		createTableIfNotExistSQL=createTable.toString();
		
		insert.append(") values(");
		int values=checkedFields+(hasPrimaryKey?-1:0);
		for(int i=0;i<values;i++){
			if(i!=0)insert.append(',');
			insert.append('?');
		}
		insert.append(')');
		insertSQL=insert.toString();
		
		
		database.execSQL(createTableIfNotExistSQL);
		
		System.out.println(String.format("Load table Reflections @%s",tableName));
		System.out.println(String.format(" createSQL:%s",createTableIfNotExistSQL));
		System.out.println(String.format("     insert:%s",insertSQL));
		System.out.println(String.format("     update:%s",updateSQL));
	}
	
	public class LineNode{
		public int index;
		public boolean isPrimaryKeyAutoIncrement;
		public String name;
		public DataType type;
		public Field field;
	}
}

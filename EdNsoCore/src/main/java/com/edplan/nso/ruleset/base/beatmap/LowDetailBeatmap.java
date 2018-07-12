package com.edplan.nso.ruleset.base.beatmap;
import com.edplan.framework.database.DatabaseLine;
import com.edplan.framework.utils.structobj.StructObject;
import org.json.JSONObject;
import org.json.JSONException;
import com.edplan.framework.database.annotation.TableName;
import com.edplan.framework.database.annotation.PrimaryKeyAutoIncrement;
import com.edplan.framework.database.DatabaseIndex;

/**
 *只包含一个.osu文件的部分信息，这些信息会被填入一张SQLite表里，都属于直接可以从.osu文件里直接获取的值
 */
@TableName("beatmap_base")
public class LowDetailBeatmap extends DatabaseLine
{
	/**
	 *在数据库里的id，用于Ruleset扩展用（每一个新的图被载入都会生成一个新的id）
	 */
	@PrimaryKeyAutoIncrement
	@DatabaseIndex(0)
	public long dbID=-1;
	
	/**
	 *文件相对路径（相对于Songs目录）
	 */
	@DatabaseIndex(1)
	public String filePath;
	
	/**
	 *文件的hash值，用来判断文件是否发生更改
	 */
	@DatabaseIndex(2)
	public String hash;
	
	/**
	 *原始Ruleset的名称
	 */
	@DatabaseIndex(3)
	public String rawRulesetName;
	
	
	/**
	 *一些信息
	 */
	@DatabaseIndex(4)
	public long beatmapSetId;

	@DatabaseIndex(5)
	public long beatmapId;

	@DatabaseIndex(6)
	public String artist;
	
	@DatabaseIndex(7)
	public String creater;
	
	@DatabaseIndex(8)
	public String title;
	
	@DatabaseIndex(9)
	public String version;

	@DatabaseIndex(10)
	public String source;
	
	/**
	 *四围信息
	 *难度星级没有包含在这部分
	 */
	@DatabaseIndex(110)
	public float hp;
	@DatabaseIndex(111)
	public float cs;
	@DatabaseIndex(112)
	public float od;
	@DatabaseIndex(113)
	public float ar;
}

package com.edplan.nso.parser;

import com.edplan.framework.utils.io.AdvancedTxtReader;
import com.edplan.framework.utils.StringUtil;
import com.edplan.framework.utils.advance.BaseDataMap;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class IniParser {

    private List<String> header = new ArrayList<>();

    private List<Page> pages = new ArrayList<>();

    private Map<String, List<Page>> pageByTag = new HashMap<>();

    public static final Pattern TAG_PATTERN = Pattern.compile(" *\\[(.*)] *");

    public IniParser() {

    }

    public List<String> getHeader() {
        return header;
    }

    public void parse(InputStream in) throws IOException {
        header.clear();
        pages.clear();

        AdvancedTxtReader reader = new AdvancedTxtReader(in);
        String line = reader.readLine();
        String tag;
        Page page;

        /* 读取文件前置信息 */
        while ((tag = parseTag(line)) == null) {
            header.add(line);
            line = reader.readLine();
            if (line == null) {
                return;
            }
        }

        /* 按tag存储文本 */
        String tmp;
        page = new Page(tag);
        addPage(page);
        line = reader.readLine();
        for (; line != null; line = reader.readLine()) {
            tmp = parseTag(line);
            if (tmp != null) {
                tag = tmp;
                page = new Page(tag);
                addPage(page);
                continue;
            }
            page.lines.add(line);
        }
    }

    public void addPage(Page page) {
        if (pages.contains(page)) {
            return;
        }
        pages.add(page);
        if (!pageByTag.containsKey(page.tag)) {
            pageByTag.put(page.tag, new ArrayList<>());
        }
        pageByTag.get(page.tag).add(page);
    }

    public StdOptionPage asOptionPage(String tag) {
        if (hasPage(tag)) {
            return new StdOptionPage(getPageByTag(tag));
        } else {
            return null;
        }
    }

    public CSVPage asCSVPage(String tag) {
        if (hasPage(tag)) {
            return new CSVPage(getPageByTag(tag));
        } else {
            return null;
        }
    }

    public Page getPageByTag(String tag) {
        return pageByTag.containsKey(tag) ? pageByTag.get(tag).get(0) : null;
    }

    public List<Page> getAllPagesByTag(String tag) {
        return pageByTag.containsKey(tag) ? new ArrayList<Page>(pageByTag.get(tag)) : new ArrayList<Page>();
    }

    public boolean hasPage(String tag) {
        return pageByTag.containsKey(tag);
    }

    public static String parseTag(String s) {
        //Matcher matcher = TAG_PATTERN.matcher(s);
        s = s.trim();
        if (s.length() > 2 && s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']') {
            return s.substring(1, s.length() - 1);
        } else {
            return null;
        }
    }


    public static final class Page {
        public String tag;

        public List<String> lines = new ArrayList<>();

        public Page(String tag) {
            this.tag = tag;
        }
    }

    public static class StdOptionPage extends BaseDataMap{

        private String tag;

        private Map<String, String> data = new HashMap<>();

        public StdOptionPage(Page page) {
            this(page, ':');
        }

        public StdOptionPage(Page page,char spl) {
            tag = page.tag;
            for (String line : page.lines) {
                line = line.trim();
                if (line.isEmpty() || StringUtil.isCommentLine(line)) {
                    continue;
                }
                int idx = line.indexOf(spl);
                if (idx == -1) {
                    continue;
                }
                data.put(
                        line.substring(0, idx).trim(),
                        line.substring(idx + 1).trim());
            }
        }

        @Override
        public Set<String> keys() {
            return data.keySet();
        }

        @Override
        public boolean hasKey(String key) {
            return data.containsKey(key);
        }

        @Override
        public String getString(String key) {
            return data.get(key);
        }

    }

    public static class CSVPage {
        private String tag;

        private List<String[]> data = new ArrayList<>();

        public CSVPage(Page page) {
            this(page, ",");
        }

        public CSVPage(Page page,String spl) {
            tag = page.tag;
            for (String line : page.lines) {
                line = line.trim();
                if (line.isEmpty() || StringUtil.isCommentLine(line)) {
                    continue;
                }
                String[] spld = line.split(spl);
                for (int i = 0; i < spld.length; i++) {
                    spld[i] = spld[i].trim();
                }
                data.add(spld);
            }
        }

        public List<String[]> getData() {
            return data;
        }
    }
}

package com.edlplan.framework.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class BufferedListResource extends AResource {
    private AResource res;

    private boolean ignoreCase = false;

    FileNode root;


    public BufferedListResource(AResource res) {
        this.res = res;
        initial();
    }

    public void setIgnoreCase(boolean ignoreCase) {
        this.ignoreCase = ignoreCase;
    }

    public boolean isIgnoreCase() {
        return ignoreCase;
    }

    private void initial() {
        try {
            root = new FileNode(null, "");
            expandFile(root);
			/*System.out.println(Arrays.toString(res.list("sb")));
			System.out.println("#expand");
			printDetails();
			System.out.println("#end expand");*/
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("err: " + e.getMessage());
        }
    }

    public void printDetails(FileNode node) {
        if (node.isFile) {
            System.out.println(node.absolutePath);
        } else {
            System.out.println(node.absolutePath + "/");
            for (FileNode f : node.list.values()) {
                printDetails(f);
            }
        }
    }

    public void printDetails() {
        printDetails(root);
    }


    private void expandFile(FileNode node) throws IOException {
        if (isFile(node.absolutePath)) {
            node.isFile = true;
        } else {
            node.isFile = false;
            node.list = new HashMap<String, FileNode>();
            String[] c = list(node.absolutePath);
            for (String n : c) {
                FileNode t = new FileNode(node, n);
                node.list.put(n, t);
                expandFile(t);
            }
        }
    }

    public InputStream openInput(String n, boolean isLowCase) throws IOException {
        if (isLowCase) {
            n = n.toLowerCase();
            FileNode node = getNodeIgnoreCase(n.split("/"), 0, root);
            return node != null ? res.openInput(node.absolutePath) : null;
        } else {
            return res.openInput(n);
        }
    }

    public FileNode getNodeIgnoreCase(String[] sp, int i, FileNode node) {
        if (i == sp.length) {
            if (node.isFile) {
                return node;
            } else {
                return null;
            }
        }
        if (node.isFile) return null;
        for (Map.Entry<String, FileNode> s : node.list.entrySet()) {
            if (s.getKey().toLowerCase().equals(sp[i])) {
                return getNodeIgnoreCase(sp, i + 1, s.getValue());
            }
        }
        return null;
    }

    @Override
    public String[] list(String dir) throws IOException {

        return res.list(dir);
    }

    @Override
    public boolean contain(String file) {

        return res.contain(file);
    }

    @Override
    public InputStream openInput(String path) throws IOException {

        return openInput(path, ignoreCase);
    }


    public class FileNode {
        public FileNode parent;
        public String absolutePath;
        public String name;
        public boolean isFile;
        public HashMap<String, FileNode> list;

        public FileNode(FileNode p, String name) {
            this.parent = p;
            this.name = name;
            this.absolutePath = (p != null && p.name.length() > 0) ? (p.absolutePath + "/" + name) : name;
        }
    }
}

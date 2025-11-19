package pt.ipp.isep.dei.pi25_26.g093.app.datastructures;

import java.util.ArrayList;

public class Tree<T extends Comparable<T>> {
    private static int K; //Dimension of the Tree

    public static class TreeNode<T extends  Comparable<T>>
    {
        private ArrayList<T> values;
        private TreeNode<T> left;
        private TreeNode<T> right;

        public TreeNode(ArrayList<T> values)
        {
            this.values = new ArrayList<>(values);
            this.left = null;
            this.right = null;
        }

        public ArrayList<T> getElements() { return values; }
        public T getElement(int e) { return values.get(e); }
        public TreeNode<T> getLeft() { return left; }
        public TreeNode<T> getRight() { return right; }

        public void setElements(ArrayList<T> values) { this.values = new ArrayList<>(values);}
        public void setLeft(TreeNode<T> leftChild) { left = leftChild; }
        public void setRight(TreeNode<T> rightChild) { right = rightChild; }

        @Override
        public String toString()
        {
            StringBuilder text = new StringBuilder("(");
            for(int i = 0; i < values.size() - 1; i++)
            {
                text.append(values.get(i)).append(", ");
            }

            text.append(values.get(values.size()-1)).append(")");

            return text.toString();
        }
    }

    private TreeNode<T> root;

    public Tree(int K)
    {
        Tree.K = K;
        root = null;
    }

    public Tree(TreeNode<T> root)
    {
        this.root = root;
    }

    protected TreeNode<T> root() {
        return root;
    }

    public boolean isEmpty(){
        return root==null;
    }

    public void Insert(ArrayList<T> values)
    {
        root = Insert(root, values, 0);
    }

    public TreeNode<T> Insert(TreeNode<T> node, ArrayList<T> values, int depth)
    {
        if(node == null)
            return new TreeNode<>(values);

        int dimension = depth % K;
        int compare = node.getElement(dimension).compareTo(values.get(dimension));

        if(compare > 0)
            node.setLeft(Insert(node.getLeft(), values, depth + 1));
        else
            node.setRight(Insert(node.getRight(), values, depth + 1));

        return node;
    }

    public int Size()
    {
        return Size(root);
    }

    public int Size(TreeNode<T> node)
    {
        if(node == null)
            return 0;

        return 1 + Size(node.getLeft()) + Size(node.getRight());
    }

    public int Height()
    {
        return Height(root, 0);
    }

    public int Height(TreeNode<T> node, int depth)
    {
        if(node == null)
            return 0;

        depth++;
        return Math.max(depth, Math.max(Height(node.getRight(), depth), Height(node.getLeft(), depth)));
    }

    public void ShowNodes()
    {
        ShowNode(root);
    }

    public void ShowNode(TreeNode<T> node)
    {
        if(node != null)
        {
            System.out.println("Node: " + node);

            if(node.getLeft() != null)
                System.out.println("Node left: " + node.getLeft());
            if(node.getRight() != null)
                System.out.println("Node right: " + node.getRight());

            ShowNode(node.getLeft());
            ShowNode(node.getRight());
        }
    }

    public TreeNode<T> Find(ArrayList<T> values)
    {
        return Find(root, values, 0);
    }

    public TreeNode<T> Find(TreeNode<T> node, ArrayList<T> values, int depth)
    {
        if(node != null)
        {
            if(node.values.equals(values))
                return node;

            int dimension = depth % K;
            int compare = node.getElement(dimension).compareTo(values.get(dimension));

            if(compare > 0)
                return Find(node.getLeft(), values, depth + 1);

            return Find(node.getRight(), values, depth + 1);
        }

        return null;
    }

    public void RangeSearch(ArrayList<T> lower, ArrayList<T> upper)
    {
        RangeSearch(root, lower, upper, 0);
    }

    public void RangeSearch(TreeNode<T> node, ArrayList<T> lower, ArrayList<T> upper, int depth)
    {
        if(node != null)
        {
            boolean inside = true;

            for(int i = 0; i < K; i++)
            {
                int lowCompare = node.getElement(i).compareTo(lower.get(i));
                int upCompare = node.getElement(i).compareTo(upper.get(i));

                if(lowCompare < 0 || upCompare > 0)
                {
                    inside = false;
                }
            }

            if(inside)
                System.out.println(node);

            int dimension = depth % K;
            int lowCompare = node.getElement(dimension).compareTo(lower.get(dimension));
            int upCompare = node.getElement(dimension).compareTo(upper.get(dimension));

            if(lowCompare > 0)
                RangeSearch(node.getLeft(), lower, upper, depth + 1);

            if(upCompare < 0)
                RangeSearch(node.getRight(), lower, upper, depth + 1);
        }
    }
}

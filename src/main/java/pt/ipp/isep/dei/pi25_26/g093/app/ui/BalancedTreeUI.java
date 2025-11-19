package pt.ipp.isep.dei.pi25_26.g093.app.ui;

import pt.ipp.isep.dei.pi25_26.g093.app.controller.BalancedTreeController;
import pt.ipp.isep.dei.pi25_26.g093.app.datastructures.Tree;
import pt.ipp.isep.dei.pi25_26.g093.app.repositories.Repositories;

import java.util.Scanner;

public class BalancedTreeUI {
    public void run()
    {
        showData();
    }

    public void showData()
    {
        Scanner sc = new Scanner(System.in);
        BalancedTreeController controller = new BalancedTreeController();

        int op;

        do{
            System.out.println("1 - Build 2D-Tree");
            System.out.println("2 - Show current 2D-Tree");
            System.out.println("0 - Back");
            System.out.print("Option: ");
            op = sc.nextInt();
            switch (op)
            {
                case 1:
                    System.out.println(controller.buildTree());
                    break;
                case 2:
                    if(controller.treeExists())
                    {
                        Tree<Double> tree = Repositories.getInstance().getCompleteStationRepository().getTree();
                        tree.ShowNodes();
                        System.out.println("All tree nodes!\n");
                    }else
                    {
                        System.out.println("There is no tree available, build one first!\n");
                    }
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Option is invalid!\n");
                    break;
            }
        }while (true);
    }
}

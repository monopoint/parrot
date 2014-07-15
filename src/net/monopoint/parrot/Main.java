package net.monopoint.parrot;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2){
            System.out.println("Invalid arguments");
            System.out.println("usage: parrot [path] [filename]");
            System.exit(0);
        }

        Parrot parrot = new Parrot(args[0], args[1]);

        System.exit(0);

    }
}

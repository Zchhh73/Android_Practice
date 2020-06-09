public class Typecast {

    public static void main(String[] args) {
        Parent parent = new Child();
        System.out.println(((Child)parent).getName());
    }
}

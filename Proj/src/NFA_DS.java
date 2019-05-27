import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NFA_DS {
    NFA_DS(){
       this.MakeNfa();
    }

    Scanner s = new Scanner(System.in);
    public List<State> AllState;
    public List<State> InitialStates = new ArrayList<>(1);
    public List<State> FinalStates = new ArrayList<>(1);
    public String[] alphabet;
    public List<Relation> relations = new ArrayList<>(1);

    private void MakeNfa(){
        try {
            AllState = new ArrayList<>(s.nextInt());
        }catch (Exception e) {
            System.out.println("Enter the number of states in first line.");
            System.out.println(e.getMessage());
        }
        alphabet = s.next().split(",");
        String[] temp;
        String rel;
        while(true) {
            rel = s.next();
            if(!rel.equals("-1")) {
                temp = rel.split(",");
                if (temp.length >= 3)
                    relations.add(
                            new Relation(AddStates(temp[0], AllState),
                                    temp[1],
                                    AddStates(temp[2], AllState))
                    );
                StateKind(temp[0]);
                StateKind(temp[2]);
            }
            else break;
        }

    }

    private void StateKind(String stateName){
        if(stateName.substring(0,2).equals("->")) {
            for (State s:InitialStates) {
                if(s.StateName.equals(stateName)){
                    return;
                }
            }
            for (State s:AllState) {
                if(s.StateName.equals(stateName)) {
                    InitialStates.add(s);
                    return;
                }
            }
            return;
        }else if(stateName.substring(0,1).equals("*")){
                for (State s:FinalStates) {
                    if(s.StateName.equals(stateName)){
                        return;
                    }
                }
                for (State s:AllState) {
                    if(s.StateName.equals(stateName)) {
                        FinalStates.add(s);
                        return;
                    }
                }
                return;
        }else return;
    }

    public static State AddStates(String stateName , List<State> states){
        if(states != null){
            for (State s:states) {
                if(s.StateName.equals(stateName) || s.StateName.substring(2).equals(stateName)) {
                    return s;
                }
            }
            State s = new State(stateName);
            states.add(s);
            return s;

        }else {
            State s1 = new State(stateName);
            states.add(s1);
            return s1;
        }
    }

}


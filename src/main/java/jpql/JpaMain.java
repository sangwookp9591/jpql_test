package jpql;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); //변수 추출하기 (Extract -> Variable) Ctrl+Alt+v

        EntityManager em = emf.createEntityManager(); //이름 일괄 변경하기 단축키 .. shift +f6 emf가 고객의 요청이 올때마다 em을 생성한다.

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{
            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamB);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush();
            em.clear();

//            String query =  "select m From Member m join fetch m.team";
//            //Member를 조인하긴 할건데 일반적 sql join을 하긴하는데 fetch라는 것으로 한번에 끌고와 끌고오는데 뭘끌고 오냐 team을 끌고와.
//
//            List<Member> result = em.createQuery(query, Member.class).getResultList();
//            for (Member member : result) {
//                System.out.println("member = " + member.getUsername()+ ", "+member.getTeam().getName());
//                //여기서 Member 에서의 team은 lazy 지연로딩 때문에 proxy로 설정되어있고, memger.getTeam()을 호출할때마다 DB에서 값을 가져온다.
//            }

//            String query = "select t From Team t join fetch t.members";
//            List<Team> result = em.createQuery(query, Team.class).getResultList();
//            for (Team team : result) {
//                System.out.println("team = " + team.getName() + "| members = " + team.getMembers().size());
//                for(Member member : team.getMembers()){
//                    System.out.println("member = " + member);
//                }
//            }

//
//            String query = "select distinct t From Team t join fetch t.members";
//            List<Team> result = em.createQuery(query, Team.class).getResultList();
//            for (Team team : result) {
//                System.out.println("team = " + team.getName() + "| members = " + team.getMembers().size());
//                for(Member member : team.getMembers()){
//                    System.out.println("member = " + member);
//                }
//            }

            //과감하게 뺸다.
            //  String query = "select m From Member m join fetch m.team t";
            String query = "select t From Team t";
            List<Team> result = em.createQuery(query, Team.class)
                    .setFirstResult(0)
                    .setMaxResults(2)
                    .getResultList();
            for (Team team : result) {
                System.out.println("team = " + team.getName() + "| members = " + team.getMembers().size());
                for(Member member : team.getMembers()){
                    System.out.println("member = " + member);
                }
            }
            tx.commit();

        } catch (Exception e){
            tx.rollback();
        }finally {
            em.close(); // EntityManager가 내부적으로 DB CONNECTION을 물고 동작하기때문에 사용다하면 자원해제를 꼭해줘야한다.
        }

        emf.close(); //WebApplication일 경우 was가 내려갈때 emf를 자원해제 해줘야한다 그래야 connection pooling이랑 resource가 내부적으로 release된다.
    }

}

package jpql;

import javax.persistence.*;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello"); //변수 추출하기 (Extract -> Variable) Ctrl+Alt+v

        EntityManager em = emf.createEntityManager(); //이름 일괄 변경하기 단축키 .. shift +f6 emf가 고객의 요청이 올때마다 em을 생성한다.

        EntityTransaction tx = em.getTransaction();

        tx.begin();

        try{
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);

            member.setTeam(team);
            em.persist(member);

            em.flush();
            em.clear();

            //String innerJoinQuery ="select m from Member m inner join  m.team t";
            //String outerJoinQuery ="select m from Member m left outer join  m.team t";
            //String setaJoinQuery = "select m from Member m, Team t where m.username = t.name";

            //조인대상 필터링
            //String query1 = "select m from Member m left join m.team t on t.name = 'teamA'";
            //연관관계 없는
            //String query2 = "select m from Member m left join Team t on m.username = t.name";




            //SELECT저절에서 서브쿼리사용 하이버네이트에서 지원
            //String query = "select (select avg(m1.age) from Member m1 ) as aveAge from Member m join Team t on m.username = t.name";

            //FROM 절의 서브쿼리는 현재 JPQL에서 사용할 수 없다.
            //String query = "select nm.age, nm.username" +
              //      " from (select m.age,m.username from Member m) as nm";

           // String query2 = "select m from Member m left join Team t on m.username = t.name";
           // List<Member> result = em.createQuery(query, Member.class).getResultList();


            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }finally {
            em.close(); // EntityManager가 내부적으로 DB CONNECTION을 물고 동작하기때문에 사용다하면 자원해제를 꼭해줘야한다.
        }

        emf.close(); //WebApplication일 경우 was가 내려갈때 emf를 자원해제 해줘야한다 그래야 connection pooling이랑 resource가 내부적으로 release된다.
    }

}

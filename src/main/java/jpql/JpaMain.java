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
            member1.setAge(0);
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member1.setAge(0);
            member2.setTeam(teamB);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member1.setAge(0);
            member3.setTeam(teamB);
            em.persist(member3);

           /* em.flush();
            em.clear();*/

            //모든 회원의 나이를 20살로 바꿔
            //FLUSH(커밋을 하거나,QUERY가나갈때, 강제로 호출할떄) 자동 호출
            int resultCount = em.createQuery("update Member m set m.age = 20")
                    .executeUpdate();

            em.clear();

            Member findMember = em.find(Member.class, member1.getId());

            System.out.println("member1.getAge() = " + member1.getAge());

            /*System.out.println("member2.getAge() = " + member2.getAge());
            System.out.println("member3.getAge() = " + member3.getAge());*/
            //이렇게 하면 영속성 컨텍스트에는 20살인게 반영이 안되있다.


            tx.commit();

        } catch (Exception e){
            tx.rollback();
        }finally {
            em.close(); // EntityManager가 내부적으로 DB CONNECTION을 물고 동작하기때문에 사용다하면 자원해제를 꼭해줘야한다.
        }

        emf.close(); //WebApplication일 경우 was가 내려갈때 emf를 자원해제 해줘야한다 그래야 connection pooling이랑 resource가 내부적으로 release된다.
    }

}

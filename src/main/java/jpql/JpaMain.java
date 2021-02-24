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
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            //내 타입정보를 정확하게 명기한 경우.
            Member singleResult1 = em.createQuery("select m from Member m where m.username = :username", Member.class)          //(쿼리, 응답정보에 대한 type class<기본적으론 entity>);
                    .setParameter("username", "member1")
                    .getSingleResult();

            System.out.println("singleResult1 = " + singleResult1);
            
            //쿼리를 날렷는데 값이 무조건 하나야 이런경우는
           // Member singleResult = query.getSingleResult();


//            List<Member> resultList = query.getResultList();
//            for (Member member1 : resultList) {
//                System.out.println("member1 = " + member1);
//            }


            //타입 정보를 명기할수 없을때는?
            //TypedQuery<Member> query = em.createQuery("select m.username, m.age from Member m"); (x)
            //username은 String이고 age는 int이다. -> 두개가달라서 명기할수 없어서 타입쿼리말고 쿼리라고 써야함.
            Query query2 = em.createQuery("select m.username, m.age from Member m");

            //username만 쓸경우 String으로 잡으면 된다. ("select m.username from Member m",String.class);

            tx.commit();
        } catch (Exception e){
            tx.rollback();
        }finally {
            em.close(); // EntityManager가 내부적으로 DB CONNECTION을 물고 동작하기때문에 사용다하면 자원해제를 꼭해줘야한다.
        }

        emf.close(); //WebApplication일 경우 was가 내려갈때 emf를 자원해제 해줘야한다 그래야 connection pooling이랑 resource가 내부적으로 release된다.
    }

}

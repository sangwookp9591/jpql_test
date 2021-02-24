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
            member.setUsername("관리자1");
            member.setAge(10);
            member.setType(MemberType.ADMIN);
            member.setTeam(team);

            em.persist(member);

            Member member2 = new Member();
            member2.setUsername("관리자2");
            member2.setAge(10);
            member2.setType(MemberType.ADMIN);
            member2.setTeam(team);

            em.persist(member2);

            em.flush();
            em.clear();

            //기본 case 식
//            String query =
//                    "select " +
//                            "case when m.age <= 10 then '학생요금' " +
//                            "     when m.age >= 60 then '경로요금' " +
//                            "     else '일반요금' " +
//                            "end" +
//                    " from Member m";
//
//            List<String> result = em.createQuery(query, String.class).getResultList();
//            for (String s : result) {
//                System.out.println("s = " + s);
//            }

//            //조건식 case 식
//            String query = "select nullif(m.username,'관리자') from Member m ";
//
//            //username이 없으면 이름없는 회원
//            List<String> resultList = em.createQuery(query, String.class).getResultList();
//
//            for (String s : resultList) {
//                System.out.println("s = " + s);
//            }

            //String query = "select concat('a', 'b') from Member m ";
            //String query = "select 'a'|| 'b' from Member m ";
            //빨간줄이나는데 빨간줄은 사실은 ||가 String 이기때문에 그렇다.
            //이럴땐 alt entet로 unInject language해주면된다.

            //일반적으로 쓸수 있는 것은 아니고  @OrderColumn  써야하는데
            // @OrderColumn 는 거의쓰는것을 추천하지 않지만 listtype의 collection에서 list의 값타입일대 값타입에줘서 쓸 수 있는데.
            //거기서 collection의 위치값을 구하는데 쓸수 있다. 거의 안쓰는게 좋다.
            // 중간에 리스트에서 빠지면 null로 들어옴.
//            @OrderColumn
//            String query = "select index(t.members) from Team t ";

            //String query = "select function('group_concat',m.username) From Member m";

            //다르게쓰는 방법 1 직관적이게 보이는 방법
            //하지만 hibernate에서는 오류가 안는데 intellij에서 오류가 나서
            // inject language를 꺼버리거나 ,injection langeuage 에서 hql(hibernate query)를 선택하자
            String query = "select group_cont(m.username) from Member m";
            List<String> resultList = em.createQuery(query, String.class).getResultList();
            for (String s : resultList) {
                System.out.println("s = " + s);
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

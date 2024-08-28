package vn.dev.managementsystem.Service;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

import org.hibernate.Session;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.stereotype.Service;
import vn.dev.managementsystem.Entity.Role;
import vn.dev.managementsystem.Entity.User;


@Service
public class RoleCustomService{
	
	@PersistenceContext
	EntityManager entityManager;
	
	public List<Role> getRoles(User user){
		// load roles by email
		StringBuilder sql = new StringBuilder().append("select r.name as name from databasems_2207.tbl_user u join tbl_user_role ur\n"
						+ " on u.id=ur.user_id join tbl_role r on r.id=ur.role_id ");
		sql.append("where 1=1 ");
		if (user.getUsername() != null) {
			sql.append(" and email = :username");
		}
		
		NativeQuery<Role> query = ((Session)entityManager.getDelegate()).createNativeQuery(sql.toString());
		
		if(user.getEmail() != null) {
			query.setParameter("username", user.getEmail());
		}
		
		query.addScalar("name", StandardBasicTypes.STRING);
		query.setResultTransformer(Transformers.aliasToBean(Role.class));
		System.out.println("IN ROLE CUSTOM SERVICE: Query get roles: " + sql);
		return query.list();
		
	}

	
	
	
}

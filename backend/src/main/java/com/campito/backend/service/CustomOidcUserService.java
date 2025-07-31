package com.campito.backend.service;

import com.campito.backend.dao.UsuarioRepository;
import com.campito.backend.model.CustomOAuth2User;
import com.campito.backend.model.ProveedorAutenticacion;
import com.campito.backend.model.Usuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class CustomOidcUserService extends OidcUserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomOidcUserService.class);

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);
        String email = oidcUser.getEmail();
        logger.info("Iniciando procesamiento de autenticacion para el email: {}", email);

        try {
            Usuario usuario = usuarioRepository.findByEmailAndProveedor(email, ProveedorAutenticacion.GOOGLE)
                    .orElseGet(() -> {
                        Usuario newUser = new Usuario();
                        newUser.setEmail(email);
                        newUser.setNombre(oidcUser.getFullName());
                        newUser.setFotoPerfil(oidcUser.getPicture());
                        newUser.setProveedor(ProveedorAutenticacion.GOOGLE);
                        newUser.setIdProveedor(oidcUser.getSubject());
                        newUser.setRol("ROL_USER");
                        newUser.setActivo(true);
                        ZoneId buenosAiresZone = ZoneId.of("America/Argentina/Buenos_Aires");
                        newUser.setFechaRegistro(LocalDateTime.now(buenosAiresZone));
                        return newUser;
                    });

            ZoneId buenosAiresZone = ZoneId.of("America/Argentina/Buenos_Aires");
            usuario.setFechaUltimoAcceso(LocalDateTime.now(buenosAiresZone));
            Usuario usuarioGuardado = usuarioRepository.save(usuario);

            // Devolvemos un principal personalizado que contiene nuestro objeto Usuario
            CustomOAuth2User customUser = new CustomOAuth2User(oidcUser, usuarioGuardado);

            logger.info("Finalizado exitosamente el procesamiento para el email: {}. ID de usuario: {}", email, usuarioGuardado.getId());
            return customUser;

        } catch (Exception e) {
            logger.error("Error critico durante la autenticacion para el email: {}. Causa: {}", email, e.getMessage(), e);
            throw e;
        }
    }
}

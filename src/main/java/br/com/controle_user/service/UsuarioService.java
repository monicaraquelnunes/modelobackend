package br.com.controle_user.service;

import br.com.controle_user.model.UsuarioModel;
import br.com.controle_user.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    public void saveUsuario(UsuarioModel usuarioModel){
        usuarioRepository.save(usuarioModel);
    }

    public Optional<UsuarioModel> updatedUsuarioModel(Long id, UsuarioModel usuarioModel){
        return usuarioRepository.findById(id)
                .map(usuarios->{
                    usuarios.setNome(usuarioModel.getNome());
                    usuarios.setCpf(usuarioModel.getCpf());
                    usuarios.setTelefone(usuarioModel.getTelefone());
                    usuarios.setEndereco(usuarioModel.getEndereco());
                    usuarios.setEmail(usuarioModel.getEmail());

                    UsuarioModel usuarioUpdated = usuarioRepository.save(usuarios);
                    return usuarioUpdated;
                });
    }

    public void deleteUsuario(Long id){
        usuarioRepository.deleteById(id);
    }

    public Optional<UsuarioModel> getUsuarioById(Long id){
        return usuarioRepository.findById(id);
    }

    public List<UsuarioModel> getAllUsuarios(){
        return usuarioRepository.findAll();
    }
}

package com.onixx.apolloveiculos.api.Services;

@Component
@Service
public class FuelsService {
    private FuelsRepository fuelsRepository;
    public FuelsService(FuelsRepository fuelsRepository) { this.fuelsRepository = fuelsRepository; }

    public List<Fuels> search() { return FuelsRepository.findAll(); }

    public List<Fuels> searchByFilters(String name, String status){
        Status statusEnum = (status != null && !status.isEmpty()) ? Status.valueOf(status.toUpperCase()) : null;
        return fuelsRepository.findByFilters(name, statusEnum);
    }
    public void buscarPorId(Integer id){}

    @Transactional
    public Fuels save(FuelsDTO fuelsDTO) {
        Fuels fuel = new Fuels(fuelsDTO.name());
        return fuelsRepository.save(fuel);
    }

    @Transactional
    public Fuels update(Long id, FuelsDTO fuelsDTO) {
        Fuels existing = fuelsRepository.findByIdfuels(id);
        if(existing == null){
            new IllegalArgumentException("Combustivel não encontrado com o id" + id);
        }

        existing.setName(fuelsDTO.name());
        existing.setStatus(fuelsDTO.status());
        return fuelsRepository.save(existing);
    }
    @Transactional
    public boolean delete(Long id) {
        Fuels fuel = fuelsRepository.findByIdFuel(id);
        if(fuel == null){
            return false;
        }
        fuelsRepository.delete(fuel);
        return true;
    }
}
}

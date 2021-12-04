package eeet2582.realestatemgt.deposit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositService {

    @Autowired
    private final DepositRepository depositRepository;

    @Autowired
    public DepositService(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }

    public List<Deposit> getAll() {
        return depositRepository.findAll();
    }

    public Deposit getById(Long depositId) {
        return depositRepository
                .findById(depositId)
                .orElseThrow(() -> new IllegalStateException("Deposit with depositId=" + depositId + " does not exist!"));
    }

    public void deleteById(Long depositId) {
        if (!depositRepository.existsById(depositId))
            throw new IllegalStateException("Deposit with depositId=" + depositId + " does not exist!");

        depositRepository.deleteById(depositId);
    }

    public List<Deposit> getByUserId(Long userId) {
        return depositRepository.findByUserHouse_UserId(userId);
    }

    public List<Deposit> getByHouseId(Long houseId) {
        return depositRepository.findByUserHouse_HouseId(houseId);
    }
}

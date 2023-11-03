package beerbrewers.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;

    @Autowired
    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    public List<Worker> getWorkers() {
        return workerRepository.findAll();
    }

    public void addNewWorker(Worker worker) {
        workerRepository.save(worker);
    }

    public void deleteWorker(Long workerId) {
        boolean exists = workerRepository.existsById(workerId);
        if (!exists) {
            throw new IllegalStateException("Worker with id '" + workerId + "' does not exist.");
        }
        workerRepository.deleteById(workerId);
    }
}

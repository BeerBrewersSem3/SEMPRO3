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

    public Worker getWorker(Long workerId) {
        return workerRepository.findByWorkerId(workerId);
    }

    public Worker getFirstWorkerInDatabase() {
        return workerRepository.findTopByOrderByWorkerIdDesc();
    }

    /**
     * Be aware that this method does not check if the worker already exists.
     * @param worker
     */
    public Worker addNewWorker(Worker worker) {
        return workerRepository.save(worker);
    }

    public boolean deleteWorker(Long workerId) {
        if (!determineIdPresenceInDB(workerId)) {
            throw new IllegalStateException("Worker with id '" + workerId + "' does not exist.");

        }
        workerRepository.deleteById(workerId);
        return true;
    }

    public boolean determineIdPresenceInDB(Long idToCheck) {
        return workerRepository.existsById(idToCheck);
    }
}

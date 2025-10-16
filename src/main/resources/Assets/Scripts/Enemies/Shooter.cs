using System.Collections;
using UnityEngine;
using UnityEngine.Rendering;  // for SortingGroup

public class Shooter : MonoBehaviour, IEnemy
{
    [Header("Projectile Settings")]
    [SerializeField] private GameObject bulletPrefab;
    [SerializeField] private float bulletSpeed = 15f;
    [SerializeField] private int burstCount = 3;
    [SerializeField, Range(0f, 90f)]
    private float angleSpread = 20f;   // max deviation from perfect aim, in degrees

    [Header("Timing")]
    [SerializeField] private float timeBetweenBursts = 0.2f;
    [SerializeField] private float restTime = 1f;

    [Header("Sorting")]
    [Tooltip("Name of the sorting layer for spawned bullets")]
    [SerializeField] private string sortingLayerName = "Default";
    [Tooltip("Order in layer for spawned bullets")]
    [SerializeField] private int sortingOrder = 0;

    private bool isShooting = false;

    public void Attack()
    {
        if (!isShooting)
            StartCoroutine(ShootRoutine());
    }

    private IEnumerator ShootRoutine()
    {
        isShooting = true;

        for (int i = 0; i < burstCount; i++)
        {
            if (PlayerController.Instance != null)
            {
                // 1) Calculate perfect aim direction
                Vector3 baseDir = (PlayerController.Instance.transform.position - transform.position).normalized;
                // 2) Apply a random angular offset to make it less accurate
                float halfSpread = angleSpread * 0.5f;
                float randomAngle = Random.Range(-halfSpread, halfSpread);
                Vector3 shotDir = Quaternion.Euler(0f, 0f, randomAngle) * baseDir;

                // 3) Spawn and orient bullet
                GameObject b = Instantiate(bulletPrefab, transform.position, Quaternion.identity);
                b.transform.right = shotDir;

                // 4) Set its speed
                if (b.TryGetComponent(out Projectile proj))
                    proj.UpdateMoveSpeed(bulletSpeed);

                // 5) Set sorting layer/order
                if (b.TryGetComponent(out SortingGroup sg))
                {
                    sg.sortingLayerName = sortingLayerName;
                    sg.sortingOrder = sortingOrder;
                }
                else if (b.TryGetComponent(out SpriteRenderer sr))
                {
                    sr.sortingLayerName = sortingLayerName;
                    sr.sortingOrder = sortingOrder;
                }
            }

            yield return new WaitForSeconds(timeBetweenBursts);
        }

        yield return new WaitForSeconds(restTime);
        isShooting = false;
    }
}
